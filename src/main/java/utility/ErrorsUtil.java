package utility;

import java.util.UUID;

public class ErrorsUtil {
    private static final String NETWORK_EQUIPMENT_NOT_INITIALIZED = "No Equipment with %s '%s' Has Been Registered";
    private static final String NO_PROGRAM_REGISTRY = "No Registry Has Been Created for the '%s' Program";
    private static final String NO_PROGRAM_DESIGN = "No Design Has Been Created for the '%s' Program";
    private static final String EXCEEDED_EQUIPMENT_LIMIT = "Cannot Fit %s %s into %s. This Violates the Equipment Design that Limits the # of %s within a %s to %s";

    public static String equipmentNotRegistered(final String name) {
        return String.format(NETWORK_EQUIPMENT_NOT_INITIALIZED , "Name" , name);
    }

    public static String equipmentNotRegistered(final UUID id) {
        return String.format(NETWORK_EQUIPMENT_NOT_INITIALIZED , "ID", id);
    }

    public static String noProgramNetworkRegistry(final String name) {
        return String.format(NO_PROGRAM_REGISTRY , name);
    }

    public static String noProgramNetworkDesign(final String name) {
        return String.format(NO_PROGRAM_DESIGN , name);
    }

    public static String equipmentLimitationExceeded(final String rootName, final String childName, final int quantity, final int max) {
        return String.format(EXCEEDED_EQUIPMENT_LIMIT , quantity , childName, rootName,childName,rootName,max);
    }
}
