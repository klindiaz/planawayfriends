package equipment.configuration;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import exception.NoSuchEquipmentException;
import utility.ErrorsUtil;
import utility.NameUtil;

import java.util.*;

public final class NetworkEquipmentRegistry {
    public static final List<EquipmentDetails> universalEquipmentDetails = new ArrayList<>();

    public final String program;
    private final BiMap<UUID,String> equipmentTypeIdToName = HashBiMap.create();

    NetworkEquipmentRegistry(String program) {
        this.program = program;
    }

    public void addEquipmentToRegistry(String name) {
        String standardizedName = NameUtil.getStandardizedName(name);
        if ( !equipmentTypeIdToName.containsValue(standardizedName) ) {
            UUID id = UUID.randomUUID();
            equipmentTypeIdToName.put( id , standardizedName );
            universalEquipmentDetails.add( new EquipmentDetails(this.program,standardizedName,id, Collections.emptyMap()) );
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

    private final class EquipmentDetails {
        String program;
        String name;
        UUID id;
        Map<String,String> details;

        EquipmentDetails(String program, String name, UUID id, Map<String,String> details) {
            this.program = program;
            this.name = name;
            this.id = id;
            this.details = details;
        }

        @Override
        public String toString() {
            return "EquipmentDetails{" +
                    "program='" + program + '\'' +
                    ", name='" + name + '\'' +
                    ", id=" + id +
                    ", details=" + details +
                    '}';
        }
    }
}
