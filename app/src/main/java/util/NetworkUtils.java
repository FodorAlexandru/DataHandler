package util;


import android.support.v4.util.Pair;

import java.util.List;
import java.util.concurrent.TimeUnit;

import common.constants.EnumConstants;

import static common.constants.EnumConstants.*;

/**
 * Created by shade_000 on 12/04/2016.
 */
public class NetworkUtils {
    //region Keys
    public static final String OPERATION_KEY = "OPERATION_KEY";
    public static final String USER_NETWORK_ERROR = "USER_NETWORK_ERROR";
    //endregion

    //region Request Constants
    public static final int TIMEOUT = 30;
    public static final TimeUnit TIMEUNIT = TimeUnit.SECONDS;
    //endregion

    //region Server Constants
    public static final String HOST="http://192.168.0.100/";//office:192.168.0.102
    public static final String URL_BASE ="NotifyMeAzure/api/";
    //endregion

    //region Controller Identifiers
    public static final String USERS_CONTROLLER_IDENTIFIER = "User";
    //endregion

    //region Base Methods

    private static String getUrlGenericBase(String URL_CONTROLLER_IDENTIFIER){
        return HOST + URL_BASE + URL_CONTROLLER_IDENTIFIER;
    }

    //region Base Methods

    private static String getMethodUrl(String controllerIdentifier,List<Pair<String,String>> params){
        String url = getUrlGenericBase(controllerIdentifier);
        if(params!=null && !params.isEmpty()){
            url += "?";
            for (Pair<String,String> pair :params) {
                url += "&" + pair.first + "=" + pair.second;
            }
        }
        return url;
    }

    //endregion

    public static String getUrl(int networkOperationIdentifier){
        NetworkOperations networkOperation = NetworkOperations.getType(networkOperationIdentifier);
        switch (networkOperation){
            case Get_Users:
                return getMethodUrl(USERS_CONTROLLER_IDENTIFIER,null);
            default:
                throw new RuntimeException("Url not found");
        }
    }
}
