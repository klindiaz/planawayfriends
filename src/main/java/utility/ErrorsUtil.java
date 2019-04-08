package utility;

import java.util.UUID;

public class ErrorsUtil {
    private static final String NETWORK_EQUIPMENT_NOT_INITIALIZED = "No Equipment with %s '%s' Has Been Registered";

    public static String equipmentNotRegistered(final String name) {
        return String.format(NETWORK_EQUIPMENT_NOT_INITIALIZED , "Name" , name);
    }

    public static String equipmentNotRegistered(final UUID id) {
        return String.format(NETWORK_EQUIPMENT_NOT_INITIALIZED , "ID", id);
    }
}
