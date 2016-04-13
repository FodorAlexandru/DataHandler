package common.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexandru on 4/13/2016.
 */
public final class EnumConstants {

    public enum NetworkOperations {
        Get_Users(1);

        //region Fields
        private int id;
        private static final Map<Integer, NetworkOperations> intToTypeMap = new HashMap<>();
        static {
            for (NetworkOperations type : NetworkOperations.values()) {
                intToTypeMap.put(type.getId(), type);
            }
        }
        //endregion

        //region Constructor
        NetworkOperations(int id) {
            this.id = id;
        }
        //endregion

        //region Get Methods

        public int getId() {
            return id;
        }

        public static NetworkOperations getType(int i) {
            return intToTypeMap.get(i);
        }

        //endregion
    }

}
