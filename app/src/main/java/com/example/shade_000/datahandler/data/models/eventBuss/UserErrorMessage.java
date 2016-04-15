package com.example.shade_000.datahandler.data.models.eventBuss;

import com.android.volley.VolleyError;

import common.base.BaseEventMessage;

/**
 * Created by shade_000 on 16/04/2016.
 */
public class UserErrorMessage extends BaseEventMessage {

    private VolleyError error;

    public UserErrorMessage(String message, VolleyError error) {
        super(message);
        this.error = error;
    }

    public VolleyError getError() {
        return error;
    }
}
