package de.dhbw.ka.se.fibo.utils.backend;

import static de.dhbw.ka.se.fibo.utils.ApiUtils.createAPIJSONRequest;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import de.dhbw.ka.se.fibo.utils.ApiUtils;
import de.dhbw.ka.se.fibo.utils.LocalDateTimeDeserializer;

public class BackendCashflowListRequest {
    private final CountDownLatch latch;
    private final JsonRequest<CashflowListResponse[]> request;
    private CashflowListResponse[] response;

    public BackendCashflowListRequest(CountDownLatch latch, String accessToken) {
        this.latch = latch;

        request = createAPIJSONRequest(CashflowListResponse[].class, ApiUtils.getBaseURL() + "/cashflows/", Request.Method.GET, null, this::onSuccess, this::onFailure, Optional.of(accessToken));
    }

    protected void onSuccess(CashflowListResponse[] response) {
        this.response = response;

        latch.countDown();
    }

    private void onFailure(VolleyError volleyError) {
        throw new RuntimeException(volleyError);
    }

    public CashflowListResponse[] getResponse() {
        return response;
    }

    public JsonRequest<CashflowListResponse[]> getRequest() {
        return request;
    }
}
