package com.example.shade_000.datahandler.data.models.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Alexandru on 4/4/2016.
 */
public class GsonRequest<T> extends JsonRequest<T>{
    private final Gson gson = new Gson();
    private final Type requestType;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;

    //region Constructor
    /**
     * Make a GET request and return a parsed object from JSON.
     *  @param url URL of the request to make
     * @param requestType Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     * @param listener
     */
    public GsonRequest(String url, Type requestType, Map<String, String> headers,String requestBody,int methodType,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(methodType, url,requestBody,listener,errorListener);
        this.requestType = requestType;
        this.headers = headers;
        this.listener = listener;
    }

    //endregion

    //region Overrides

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
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            T parseObject = gson.fromJson(json, requestType);
            return Response.success(
                    parseObject,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            volleyError = new VolleyError(new String(volleyError.networkResponse.data));
        }
        return volleyError;
    }

    //endregion
}
