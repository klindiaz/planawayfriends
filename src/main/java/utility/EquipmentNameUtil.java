package utility;

public final class EquipmentNameUtil {
    public static String getStandardizedName(String name) {
        return name.toUpperCase().trim().replaceAll(" ","_");
    }
}
