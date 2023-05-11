package de.dhbw.ka.se.fibo.utils.backend;

import static de.dhbw.ka.se.fibo.utils.ApiUtils.createAPIJSONRequest;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonRequest;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class BackendPlacesListRequest extends AbstractBackendListRequest<PlacesListResponse> {
    public BackendPlacesListRequest(CountDownLatch latch, String accessToken) {
        super(latch, accessToken);
    }

    @Override
    protected JsonRequest<PlacesListResponse[]> populateRequest(String accessToken) {
        return createAPIJSONRequest(PlacesListResponse[].class, "/places/", Request.Method.GET, null, this::onSuccess, this::onFailure, Optional.of(accessToken));
    }
}
