package common.base;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alexandru on 4/13/2016.
 */
public class BaseModel implements Serializable {
    //region Fields
    @SerializedName("Id")
    private int _id;
    //endregion Fields

    //region Get Methods

    public int getId() {
        return _id;
    }

    //region Constructors

    public BaseModel() {
    }

    public BaseModel(int _id) {
        this._id = _id;
    }

    //endregion Constructors

    //region

    //region Overrides

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseModel)) return false;

        BaseModel modelBase = (BaseModel) o;
        return _id == modelBase._id;
    }

    @Override
    public int hashCode() {
        return _id;
    }

    //endregion Overrides
}
