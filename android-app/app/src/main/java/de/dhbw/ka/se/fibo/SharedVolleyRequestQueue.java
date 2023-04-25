package de.dhbw.ka.se.fibo;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SharedVolleyRequestQueue {

    private static SharedVolleyRequestQueue instance;
    private RequestQueue requestQueue;
    private static Context context;

    private SharedVolleyRequestQueue(Context context) {
        SharedVolleyRequestQueue.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized SharedVolleyRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new SharedVolleyRequestQueue(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
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


}
