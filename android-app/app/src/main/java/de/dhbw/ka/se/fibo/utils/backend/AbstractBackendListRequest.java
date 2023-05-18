package de.dhbw.ka.se.fibo.utils.backend;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;

import java.util.concurrent.CountDownLatch;

public abstract class AbstractBackendListRequest<T> {

    private final CountDownLatch latch;
    private final JsonRequest<T[]> request;
    private T[] response;

    private VolleyError error;

    public AbstractBackendListRequest(CountDownLatch latch, String accessToken) {
        this.latch = latch;

        request = populateRequest(accessToken);
        request.setRetryPolicy(new DefaultRetryPolicy( 5000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
