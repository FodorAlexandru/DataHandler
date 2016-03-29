package common.constants;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by shade_000 on 3/26/2016.
 */
public class UriConstants {

    public static final String CONTENT_AUTHORITY = "com.example.shade_000.cleanarhitecture.access.provider";

    //region User
    /**
     * URI ID for route: /users
     */
    public static final int ROUTE_USERS = 1;

    /**
     * URI ID for route: /users/{ID}
     */
    public static final int ROUTE_USERS_ID = 2;

    /**
     * Base Path for the Content_URI
     */
    public static final String USER_BASE_PATH = "users";

    /**
     * User Contet Uri formed from scheme + authority + path
     */
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY
            + "/" + USER_BASE_PATH);

    /**
     * Mime tipes for multiple which is dir and for on item
     */
    public static final String USER_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/mt-user";
    public static final String USER_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/mt-user";
    //endregion

}
