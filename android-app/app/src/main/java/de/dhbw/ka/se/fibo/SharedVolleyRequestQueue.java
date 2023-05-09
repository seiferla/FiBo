package de.dhbw.ka.se.fibo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SharedVolleyRequestQueue {

    private static SharedVolleyRequestQueue instance;
    private RequestQueue requestQueue;
    private static Context context;
    private CountingIdlingResource mIdlingResource;

    private final String TAG = "SharedVolleyRequestQueue";

    private SharedVolleyRequestQueue(Context context) {
        SharedVolleyRequestQueue.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized SharedVolleyRequestQueue getInstance(Context context) {
        if (null == SharedVolleyRequestQueue.instance) {
            SharedVolleyRequestQueue.instance = new SharedVolleyRequestQueue(context);
        }
        return SharedVolleyRequestQueue.instance;
    }

    private RequestQueue getRequestQueue() {
        if (null == requestQueue) {
            requestQueue = Volley.newRequestQueue(SharedVolleyRequestQueue.context.getApplicationContext());
            requestQueue.start();

            // clear context out of scope to reduce the likelihood of memory leaking
            SharedVolleyRequestQueue.context = null;
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    /**
     * Only called from test, creates and returns a new {@link CountingIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public synchronized IdlingResource getIdlingResource() {
        if (null == mIdlingResource) {
            mIdlingResource = new CountingIdlingResource("volley_http_request_idling_resource_counter");

            Log.i(TAG, "CountingIdlingResource() created");

            getRequestQueue().addRequestEventListener((Request<?> request, @RequestQueue.RequestEvent int event) -> {
                if (RequestQueue.RequestEvent.REQUEST_QUEUED == event) {
                    mIdlingResource.increment();
                    Log.i(TAG, "new http request. Incremented idling resource counter");
                } else if (RequestQueue.RequestEvent.REQUEST_FINISHED == event) {
                    mIdlingResource.decrement();
                    Log.i(TAG, "http request finished. Decremented idling resource counter");
                }
            });

            Log.i(TAG, "added request event listener for idling resource");
        }
        return mIdlingResource;
    }
}
