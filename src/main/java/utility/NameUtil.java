package utility;

public final class NameUtil {
    public static String getStandardizedName(String name) {
        return name.toUpperCase().trim().replaceAll(" ","_");
    }
}
