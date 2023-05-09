package de.dhbw.ka.se.fibo.utils.backend;

import static de.dhbw.ka.se.fibo.utils.ApiUtils.createAPIJSONRequest;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class BackendPlacesListRequest {
    private final CountDownLatch latch;
    private final JsonRequest<PlacesListResponse[]> request;
    private PlacesListResponse[] response;

    public BackendPlacesListRequest(CountDownLatch latch, String accessToken) {
        this.latch = latch;

        request = createAPIJSONRequest(PlacesListResponse[].class, "/places/", Request.Method.GET, null, this::onSuccess, this::onFailure, Optional.of(accessToken));
    }

    protected void onSuccess(PlacesListResponse[] response) {
        this.response = response;

        latch.countDown();
    }

    private void onFailure(VolleyError volleyError) {
        throw new RuntimeException(volleyError);
    }

    public PlacesListResponse[] getResponse() {
        return response;
    }

    public JsonRequest<PlacesListResponse[]> getRequest() {
        return request;
    }
}
