package planner;

import equipment.configuration.NetworkEquipmentDesign;
import equipment.configuration.NetworkEquipmentRegistry;

import java.util.Map;
import java.util.UUID;

public class NetworkEngineer {

    public NetworkEngineer(final String program) {
        this.program = program;
    }

    private final String program;
    private final NetworkEquipmentRegistry registry = new NetworkEquipmentRegistry();
    private final NetworkEquipmentDesign design = new NetworkEquipmentDesign(registry);

    public Map<UUID,Integer> get(UUID root , UUID nthChild) {
        return null;
    }

    public NetworkEquipmentRegistry getRegistry() {
        return registry;
    }

    public NetworkEquipmentDesign getDesign() {
        return design;
    }
}
