package com.example.shade_000.datahandler.data.models.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import java.util.Map;

/**
 * Created by Alexandru on 4/4/2016.
 */
public class SimpleRequest<T> extends JsonRequest<T> {
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *  @param url URL of the request to make
     * @param headers Map of request headers
     * @param listener
     */
    public SimpleRequest(String url, Map<String, String> headers, String requestBody, int methodType,
                               Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(methodType, url,requestBody,listener,errorListener);
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return (Response<T>) Response.success(json,
                    HttpHeaderParser.parseCacheHeaders(response));
        }catch (Exception e){
            return null;
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            volleyError = new VolleyError(new String(volleyError.networkResponse.data));
        }
        return volleyError;
    }
}

