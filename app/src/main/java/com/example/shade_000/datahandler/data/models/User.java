package com.example.shade_000.datahandler.data.models;

import common.base.BaseModel;

/**
 * Created by Alexandru on 4/13/2016.
 */
public class User extends BaseModel{
    //region Fields
    private String name;
    private String alias;
    private String email;
    private String phone;
    //endregion Fields

    //region Constructors

    public User(int _id, String alias, String email, String name, String phone) {
        super(_id);
        this.alias = alias;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public User() {
    }

    //endregion Constructors

    //region Get Methods

    public String getAlias() {
        return alias;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    //endregion Get Methods

}
