package de.dhbw.ka.se.fibo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import de.dhbw.ka.se.fibo.strategies.LoginStrategyProduction;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

public class TestHelper {
    public static void checkRegisterRequestResponse(RecordedRequest actualRequest, String email, String password) throws UnsupportedEncodingException {
        assertNotNull(actualRequest);
        assertNotNull(actualRequest.getRequestUrl());
        assertEquals("/users/register/", actualRequest.getRequestUrl().encodedPath());

        Map<String, List<String>> requestBody = TestHelper.getBodyString(actualRequest.getBody());
        assertEquals(List.of(email), requestBody.get("email"));
        assertEquals(List.of(password), requestBody.get("password"));
    }

    public static void checkLoginRequest(RecordedRequest actualRequest, String email, String password) {
        assertNotNull(actualRequest);
        assertNotNull(actualRequest.getRequestUrl());
        assertEquals("/users/login/", actualRequest.getRequestUrl().encodedPath());

        JsonObject requestBody = TestHelper.getJsonString(actualRequest.getBody());
        assertEquals(email, requestBody.get("username").getAsString());
        assertEquals(password, requestBody.get("password").getAsString());
    }

    public static void checkDeleteUserRequest(RecordedRequest actualRequest) {
        assertNotNull(actualRequest);
        assertNotNull(actualRequest.getRequestUrl());
        assertEquals("/users/delete/", actualRequest.getRequestUrl().encodedPath());
    }

    private static Map<String, List<String>> getBodyString(Buffer he) throws UnsupportedEncodingException {
        Map<String, List<String>> parameters = new HashMap<>();

        String query;
        try (InputStream body = he.getBuffer().inputStream()) {
            query = new String(IOUtils.toByteArray(body), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] keyValuePairs = query.split("&");
        for (String keyValuePair : keyValuePairs) {
            String[] keyAndValue = keyValuePair.split("=", 2);

            String key = keyAndValue[0];
            String value = 1 < keyAndValue.length ? keyAndValue[1] : "";

            key = URLDecoder.decode(key, "utf-8");
            value = URLDecoder.decode(value, "utf-8");

            parameters.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }

        return parameters;
    }

    private static JsonObject getJsonString(Buffer he) {
        String query;
        try (InputStream body = he.getBuffer().inputStream()) {
            query = new String(IOUtils.toByteArray(body), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Gson().fromJson(query, JsonObject.class);
    }

    public static String getTokensAsJsonString() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
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
                .setExpiration(Date.from(LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.UTC)))
                .signWith(key)
                .compact();

        LoginStrategyProduction.LoginResponse loginResponse = new LoginStrategyProduction.LoginResponse(refreshToken, accessToken);

        return new Gson().toJson(loginResponse);
    }
}
