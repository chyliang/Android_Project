package com.example.fishcatcher;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RQueue {
    private static RQueue instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private RQueue(Context context){
        ctx = context;
    }

    public static synchronized RQueue getInstance(Context context) {
        if (instance == null) {
            instance = new RQueue(context);
        }
        return instance;
    }

    RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
