package equipment.configuration.factory;

import equipment.configuration.NetworkEquipmentDesign;
import equipment.configuration.NetworkEquipmentRegistry;
import exception.NoSuchProgramException;
import utility.ErrorsUtil;
import utility.NameUtil;

import java.util.HashMap;
import java.util.Map;

public class NetworkDesignFactory {
    private static Map<String, NetworkEquipmentDesign> designs = new HashMap<>();

    public static NetworkEquipmentDesign getNetworkEquipmentDesign(String program) {
        String standardizedName = NameUtil.getStandardizedName(program);
        if ( !designs.containsKey(standardizedName) ) {
            throw new NoSuchProgramException(ErrorsUtil.noProgramNetworkDesign(program));
        }
        return designs.get( standardizedName );
    }

    public static NetworkEquipmentDesign createNetworkEquipmentDesign(String program) {
        String standardizedName = NameUtil.getStandardizedName(program);
        NetworkEquipmentDesign result =
                                        new NetworkEquipmentDesign(
                                                NetworkRegistryFactory.getNetworkEquipmentRegistry(program)
                                        );
        designs.put(standardizedName , result);

        return result;
    }
}
