package de.dhbw.ka.se.fibo.sync;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import android.content.Context;
import android.util.Log;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import de.dhbw.ka.se.fibo.ApplicationState;
import de.dhbw.ka.se.fibo.BuildConfig;
import de.dhbw.ka.se.fibo.MainActivity;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.SharedVolleyRequestQueue;
import de.dhbw.ka.se.fibo.strategies.LoginStrategyProduction;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class BackendSynchronizationTest {

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    private MockWebServer server = new MockWebServer();

    private Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @BeforeClass
    public static void initResourceIdling() {
        Log.i("FiBo", "initalizing resource idling…");

        IdlingRegistry.getInstance().register(
                SharedVolleyRequestQueue.getInstance(
                        InstrumentationRegistry.getInstrumentation().getTargetContext()
                ).getIdlingResource()
        );
    }

    @Before
    public void setUp() throws IOException {
        server.start(8000);
    }

    @After
    public void tearDown() throws IOException {
        server.close();

        // reset everything related to JWT handling to make sure every tests runs atomic
        ApplicationState.getInstance(appContext).clearAuthorization();
        ApplicationState.getInstance(appContext).setJwsSigningKey(BuildConfig.JWS_SIGNING_KEY.getBytes());
    }

    @Test
    public void testSwipeDown() throws InterruptedException {
        // given
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        ApplicationState.getInstance(appContext).setJwsSigningKey(key.getEncoded());

        String refreshToken = Jwts.builder()
                .setClaims(Map.of(
                        "token_type", "refresh",
                        "user_id", 1
                ))
                .setExpiration(Date.from(LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.UTC)))
                .signWith(key)
                .compact();

        String accessToken = Jwts.builder()
                .setClaims(Map.of(
                        "token_type", "access",
                        "user_id", 1
                ))
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(5).toInstant(ZoneOffset.UTC)))
                .signWith(key)
                .compact();

        ApplicationState.getInstance(appContext).storeAuthorization(new LoginStrategyProduction.LoginResponse(refreshToken, accessToken));

        Dispatcher dispatcher = new Dispatcher() {
            @Override
            @NotNull
            public MockResponse dispatch(RecordedRequest request) {
                switch (Objects.requireNonNull(request.getPath())) {
                    case "/categories/":
                        return new MockResponse().setResponseCode(200).setBody("[{\"id\":1,\"name\":\"Essen\",\"account\":1}]");
                    case "/cashflows/":
                        return new MockResponse().setResponseCode(200).setBody("[{\"id\":1,\"is_income\":false,\"overall_value\":\"12.00\",\"created\":\"2023-05-18T11:45:48.758685Z\",\"updated\":\"2023-05-18T11:45:48.758685Z\",\"category\":1,\"place\":1,\"account\":1}]");
                    case "/places/":
                        return new MockResponse().setResponseCode(200).setBody("[{\"id\":1,\"address\":\"Am dm-Platz 1\",\"name\":\"dm\"}]");
                }
                fail("Path is not one of /categories/, /cashflows/, /places/");
                return null;
            }
        };
        server.setDispatcher(dispatcher);

        // when
        onView(withId(R.id.swipeContainer)).perform(swipeDown());

        // then
        List<RecordedRequest> requests = new ArrayList<>();
        requests.add(server.takeRequest(30, TimeUnit.SECONDS));
        requests.add(server.takeRequest(30, TimeUnit.SECONDS));
        requests.add(server.takeRequest(30, TimeUnit.SECONDS));

        requests.forEach(recordedRequest -> {
            assertNotNull(recordedRequest);
            assertEquals("Bearer " + accessToken, recordedRequest.getHeader("Authorization"));
        });

        onView(withId(R.id.materialCardViewContainer)).check(matches(isDisplayed()));

        onView(withId(R.id.cardTitle)).check(matches(withText("dm")));

        onView(withId(R.id.cashFlowValue)).check(matches(withText("-12,00 €")));

        onView(withId(R.id.date)).check(matches(withText("18.05.2023")));

        onView(withId(R.id.initial)).check(matches(withText("E")));
    }
}
