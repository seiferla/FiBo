package de.dhbw.ka.se.fibo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

public class TestHelper {
    public static void checkLoginRequestResponse(RecordedRequest actualRequest, String email, String password) {
        // do some checks to increase the likelihood the UI changes
        // and we get redirected because of the successful login
        assertNotNull(actualRequest);
        assertNotNull(actualRequest.getRequestUrl());
        assertEquals("/users/login/", actualRequest.getRequestUrl().encodedPath());

        JsonObject requestBody = TestHelper.getJsonString(actualRequest.getBody());
        assertEquals(email, requestBody.get("username").getAsString());
        assertEquals(password, requestBody.get("password").getAsString());
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

    public static void checkRegisterRequestResponse(RecordedRequest request, String email) {
    }
}
