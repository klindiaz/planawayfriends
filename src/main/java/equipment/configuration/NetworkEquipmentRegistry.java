package equipment.configuration;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import equipment.Equipment;
import equipment.implementation.NetworkEquipment;
import exception.NoSuchEquipmentException;
import utility.NameUtil;
import utility.ErrorsUtil;

import java.util.UUID;

public final class NetworkEquipmentRegistry {

    private final BiMap<UUID,String> equipmentTypeIdToName = HashBiMap.create();

    public void addEquipmentToRegistry(String name) {
        String standardizedName = NameUtil.getStandardizedName(name);
        if ( !equipmentTypeIdToName.containsValue(standardizedName) ) {
            equipmentTypeIdToName.put( UUID.randomUUID() , standardizedName );
        }
    }

    public UUID getEquipmentTypeID(final String name) throws NoSuchEquipmentException {
        UUID result;
        String standardizedName = NameUtil.getStandardizedName(name);
        result = equipmentTypeIdToName.inverse().get( standardizedName );

        if (result == null) {
            throw new NoSuchEquipmentException(ErrorsUtil.equipmentNotRegistered(name));
        }
        return result ;
    }

    public String getEquipmentTypeName(final UUID id) throws NoSuchEquipmentException {
        String result;
        result = equipmentTypeIdToName.get(id);

        if (result == null) {
            throw new NoSuchEquipmentException(ErrorsUtil.equipmentNotRegistered(id));
        }
        return result ;
    }

    public Equipment getEquipmentInstance(final String name) {
        return getEquipmentInstance( getEquipmentTypeID(name) );
    }

    public Equipment getEquipmentInstance(final UUID uuid) {
        String name = equipmentTypeIdToName.get(uuid);
        return new NetworkEquipment(name);
    }
}
