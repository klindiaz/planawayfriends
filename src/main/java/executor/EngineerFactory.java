package executor;

import utility.NameUtil;

import java.util.HashMap;
import java.util.Map;

public class EngineerFactory {
    private static final Map<String,NetworkEngineer> engineers = new HashMap<>();

    public static NetworkEngineer getNetworkEngineer(final String program) {
        String standardizedName = NameUtil.getStandardizedName(program);

        if ( !engineers.containsKey(standardizedName) ) {
            engineers.put( standardizedName , new NetworkEngineer(standardizedName) );
        }
        return engineers.get(standardizedName);
    }
}
