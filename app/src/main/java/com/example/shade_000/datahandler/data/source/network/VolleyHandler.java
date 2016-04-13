package com.example.shade_000.datahandler.data.source.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Alexandru on 4/13/2016.
 */
public class VolleyHandler {
    //region Fields
    private static VolleyHandler instance;
    private RequestQueue requestQueue;
    private Context context;
    //endregion

    //region Get Methods

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    //endregion Get Methods

    //region Constructor

    private VolleyHandler(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyHandler getInstance(Context context){
        if(instance == null){
            instance = new VolleyHandler(context);
        }
        return instance;
    }

    //endregion Constructor

    //region Methods

    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }

    //endregion Methods
}
