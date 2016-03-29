package common.constants;

/**
 * Created by shade_000 on 30/03/2016.
 */
public final class DatabaseContract {
    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "DatabaseHandler.db";
    //region Types
    private static final String TEXT_TYPE          = " TEXT";
    private static final String INTEGER_TYPE       = " INTEGER";
    //endregion Types
    //region Separators
    private static final String COMMA_SEP          = ",";
    public static final String SLASH = "/";
    //endregion
    //region Provider
    public static final String AUTHORITY = "com.example.shade_000.datahandler.access.provider";
    public static final String SCHEME = "content://";
    //endregion
}
