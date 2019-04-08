package equipment.configuration;

import equipment.Equipment;
import equipment.implementation.NetworkEquipment;

import java.util.UUID;

public class NetworkEquipmentFactory {

    public static Equipment getEquipmentInstance(final String program , final UUID equipmentTypeId) {
        String equipmentTypeName = NetworkRegistryFactory
                                            .getNetworkEquipmentRegistry(program)
                                            .getEquipmentTypeName(equipmentTypeId);

        return new NetworkEquipment(program , equipmentTypeName , equipmentTypeId);
    }
}
