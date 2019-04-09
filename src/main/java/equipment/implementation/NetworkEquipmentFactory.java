package equipment.implementation;

import equipment.Equipment;
import equipment.configuration.NetworkEquipmentRegistry;
import equipment.configuration.NetworkRegistryFactory;

import java.util.UUID;

public class NetworkEquipmentFactory {

    public static Equipment getEquipmentInstance(final String program , final UUID equipmentTypeId) {
        NetworkEquipmentRegistry registry = NetworkRegistryFactory.getNetworkEquipmentRegistry(program);
        String equipmentTypeName = registry.getEquipmentTypeName(equipmentTypeId);

        return new NetworkEquipment(program , equipmentTypeName , equipmentTypeId);
    }

    public static Equipment getEquipmentInstance(final String program , final String equipmentTypeName) {
        NetworkEquipmentRegistry registry = NetworkRegistryFactory.getNetworkEquipmentRegistry(program);
        UUID equipmentTypeId = registry.getEquipmentTypeID(equipmentTypeName);

        return new NetworkEquipment(program , equipmentTypeName , equipmentTypeId);
    }
}
