package planner;

import equipment.configuration.NetworkEquipmentDesign;
import equipment.configuration.NetworkEquipmentRegistry;

import java.util.Map;
import java.util.UUID;

public class NetworkEngineer {
    private static NetworkEquipmentRegistry registry = new NetworkEquipmentRegistry();
    private static NetworkEquipmentDesign design = new NetworkEquipmentDesign();

    public static Map<UUID,Integer> get(UUID root , UUID nthChild) {

        return null;
    }
}
