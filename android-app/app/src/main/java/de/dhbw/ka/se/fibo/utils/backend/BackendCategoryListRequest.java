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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import de.dhbw.ka.se.fibo.utils.ApiUtils;

public class BackendCategoryListRequest {
    private final CountDownLatch latch;
    private final JsonRequest<CategoryListResponse[]> request;
    private CategoryListResponse[] response;

    public BackendCategoryListRequest(CountDownLatch latch, String accessToken) {
        this.latch = latch;

        request = createAPIJSONRequest(CategoryListResponse[].class, ApiUtils.getBaseURL() + "/categories/", Request.Method.GET, null, this::onSuccess, this::onFailure, Optional.of(accessToken));
    }

    protected void onSuccess(CategoryListResponse[] response) {
        this.response = response;

        latch.countDown();
    }

    private void onFailure(VolleyError volleyError) {
        throw new RuntimeException(volleyError);
    }

    public CategoryListResponse[] getResponse() {
        return response;
    }

    public JsonRequest<CategoryListResponse[]> getRequest() {
        return request;
    }
}
