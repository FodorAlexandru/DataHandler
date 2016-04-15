package com.example.shade_000.datahandler.data.source.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.example.shade_000.datahandler.data.models.User;
import com.example.shade_000.datahandler.data.source.network.VolleyHandler;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by shade_000 on 15/04/2016.
 */
public abstract class RequestProcessor<T> implements Runnable {

    //region Fields
    private int networkOperationIdentifier;
    private Context context;
    private long timeout;
    private TimeUnit unit;
    //endregion

    //region Constructor

    public RequestProcessor(int networkOperationIdentifier, Context context, long timeout, TimeUnit unit) {
        this.networkOperationIdentifier = networkOperationIdentifier;
        this.context = context;
        this.timeout = timeout;
        this.unit = unit;
    }


    //endregion

    //region Overrides

    @Override
    public void run() {
        RequestFuture<T> requestFuture = RequestFuture.newFuture();
        Request requestToAdd = getRequest(requestFuture);
        VolleyHandler.getInstance(context).addToRequestQueue(requestToAdd);

        T result = null;
        try {
            result = requestFuture.get(timeout, unit);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            onError(new VolleyError(e));
            return;
        }
        if(result!=null){
            onResult(result);
        }
    }

    //endregion

    //region Abstract Methods

    public abstract Request getRequest(RequestFuture<T> requestFuture);

    protected abstract void onError(Exception e);

    protected abstract void onResult(T result);

    //endregion Abstract Methods

    //region Get Methods

    public int getNetworkOperationIdentifier() {
        return networkOperationIdentifier;
    }

    //endregion
}
