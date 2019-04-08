package equipment.configuration.factory;

import equipment.configuration.NetworkEquipmentDesign;
import equipment.configuration.NetworkEquipmentRegistry;
import exception.NoSuchProgramException;
import utility.ErrorsUtil;
import utility.NameUtil;

import java.util.HashMap;
import java.util.Map;

public class NetworkRegistryFactory {
    private static Map<String, NetworkEquipmentRegistry> registries = new HashMap<>();

    public static NetworkEquipmentRegistry getNetworkEquipmentRegistry(String program) {
        String standardizedName = NameUtil.getStandardizedName(program);
        if ( !registries.containsKey(standardizedName) ) {
            throw new NoSuchProgramException(ErrorsUtil.noProgramNetworkRegistry(program));
        }
        return registries.get( standardizedName );
    }

    public static NetworkEquipmentRegistry createNetworkEquipmentRegistry(String program) {
        String standardizedName = NameUtil.getStandardizedName(program);
        NetworkEquipmentRegistry registry = new NetworkEquipmentRegistry();
        registries.put(standardizedName , registry);

        return registry;
    }
}
