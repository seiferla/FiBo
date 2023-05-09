package de.dhbw.ka.se.fibo.utils.backend;

import static de.dhbw.ka.se.fibo.utils.ApiUtils.createAPIJSONRequest;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import de.dhbw.ka.se.fibo.utils.ApiUtils;

public class BackendCategoryListRequest {
    private final CountDownLatch latch;
    private final JsonRequest<CategoryListResponse[]> request;
    private CategoryListResponse[] response;

    public BackendCategoryListRequest(CountDownLatch latch, String accessToken) {
        this.latch = latch;

        request = createAPIJSONRequest(CategoryListResponse[].class, "/categories/", Request.Method.GET, null, this::onSuccess, this::onFailure, Optional.of(accessToken));
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
