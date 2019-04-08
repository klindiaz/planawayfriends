package equipment.configuration;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import equipment.Equipment;
import equipment.implementation.NetworkEquipment;
import exception.NoSuchEquipmentException;
import utility.EquipmentNameUtil;
import utility.ErrorsUtil;

import java.util.UUID;

public final class NetworkEquipmentRegistry {

    private static final BiMap<UUID,String> equipmentTypeIdToName = HashBiMap.create();

    public static void addEquipmentToRegistry(String name) {
        String standardizedName = EquipmentNameUtil.getStandardizedName(name);
        if ( !equipmentTypeIdToName.containsValue(standardizedName) ) {
            equipmentTypeIdToName.put( UUID.randomUUID() , standardizedName );
        }
    }

    public static UUID getEquipmentTypeID(final String name) throws NoSuchEquipmentException {
        UUID result;
        String standardizedName = EquipmentNameUtil.getStandardizedName(name);
        result = equipmentTypeIdToName.inverse().get( standardizedName );

        if (result == null) {
            throw new NoSuchEquipmentException(ErrorsUtil.equipmentNotRegistered(name));
        }
        return result ;
    }

    public static String getEquipmentTypeName(final UUID id) throws NoSuchEquipmentException {
        String result;
        result = equipmentTypeIdToName.get(id);

        if (result == null) {
            throw new NoSuchEquipmentException(ErrorsUtil.equipmentNotRegistered(id));
        }
        return result ;
    }

    public static Equipment getEquipmentInstance(final String name) {
        return getEquipmentInstance( getEquipmentTypeID(name) );
    }

    public static Equipment getEquipmentInstance(final UUID uuid) {
        String name = equipmentTypeIdToName.get(uuid);
        return new NetworkEquipment(name);
    }
}
