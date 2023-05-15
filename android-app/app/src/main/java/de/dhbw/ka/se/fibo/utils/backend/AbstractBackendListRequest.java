package de.dhbw.ka.se.fibo.utils.backend;

import static de.dhbw.ka.se.fibo.utils.ApiUtils.createAPIJSONRequest;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public abstract class AbstractBackendListRequest<T> {

    private final CountDownLatch latch;
    private final JsonRequest<T[]> request;
    private T[] response;

    private VolleyError error;

    public AbstractBackendListRequest(CountDownLatch latch, String accessToken) {
        this.latch = latch;

        request = populateRequest(accessToken);
    }

    protected abstract JsonRequest<T[]> populateRequest(String accessToken);


    protected void onSuccess(T[] response) {
        this.response = response;

        latch.countDown();
    }

    protected void onFailure(VolleyError volleyError) {
        error = volleyError;

        latch.countDown();
    }

    public VolleyError getError() {
        return error;
    }

    public T[] getResponse() throws VolleyError {
        if (null != error) {
            throw error;
        }

        return response;
    }

    public JsonRequest<T[]> getRequest() {
        return request;
    }
}
