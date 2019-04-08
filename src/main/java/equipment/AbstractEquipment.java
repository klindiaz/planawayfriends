package equipment;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import equipment.configuration.NetworkDesignFactory;
import equipment.configuration.NetworkRegistryFactory;
import equipment.implementation.NetworkEquipment;
import equipment.repository.NetworkEquipmentRepository;
import utility.NameUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractEquipment implements Equipment {
    private final String program;
    private final String uniqueName;
    private final UUID equipmentTypeId;
    private final String equipmentTypeName;
    private final Multiset<UUID> quantityOfChildren = HashMultiset.create();
    private final Map<String,Integer> equipmentNameToCount = new HashMap<>();

    public AbstractEquipment() {
        this.program = "NONE";
        this.uniqueName = "NONE";
        this.equipmentTypeName = "NULL OBJECT PATTERN";
        this.equipmentTypeId = UUID.randomUUID();
    }

    public AbstractEquipment(String program , String name , UUID id) {
        this.program = NameUtil.getStandardizedName(program);
        this.equipmentTypeName = NameUtil.getStandardizedName(name);
        this.equipmentTypeId = id;

        NetworkEquipmentRepository.addOccurrence( this.equipmentTypeId );
        this.uniqueName = this.equipmentTypeName + " - " + NetworkEquipmentRepository.getOccurrences(this.equipmentTypeId);
    }

    @Override
    public boolean isFull(UUID equipmentType) {
        return isNone() || !canAddEquipment(equipmentType);
    }

    @Override
    public boolean canAddEquipment(UUID equipmentType) {
        return canAddEquipment(equipmentType , 1);
    }

    @Override
    public boolean canAddEquipment(Equipment equipment) {
        return canAddEquipment(equipment , 1);
    }

    @Override
    public boolean canAddEquipment(Equipment equipment , int quantity) {
        return canAddEquipment(equipment.getEquipmentTypeId() , quantity);
    }

    @Override
    public boolean addEquipment(String name) {
        return addEquipment( name , 1 );
    }

    @Override
    public boolean addEquipment(String name , int quantity) {
        return addEquipment(
                NetworkRegistryFactory.getNetworkEquipmentRegistry(this.program).getEquipmentTypeID(name),
                quantity
        );
    }

    @Override
    public boolean canAddEquipment(UUID equipmentTypeId , int quantity) {
        int newProposedQuantity = getQuantityOfChild(equipmentTypeId) + quantity;
        int max = NetworkDesignFactory
                .getNetworkEquipmentDesign(this.program)
                .getMaxQuantityOfChild(this.getEquipmentTypeId() , equipmentTypeId);
        return !isNone() || (newProposedQuantity <= max);
    }

    @Override
    public boolean addEquipment(Equipment equipment) {
        boolean result = false;
        if (!equipment.isNone() && canAddEquipment(equipment)) {
            addChildEquipment(equipment);
            result = true;
        }
        return result;
    }

    @Override
    public boolean addEquipment(UUID equipmentTypeId) {
        return addEquipment( equipmentTypeId , 1);
    }

    @Override
    public boolean addEquipment(UUID equipmentTypeId , int quantity) {
        boolean result = false;
        if ( equipmentTypeId != NetworkEquipment.NONE.getEquipmentTypeId() && canAddEquipment(equipmentTypeId , quantity) ) {
            for (int i = 0 ; i < quantity ; i++) {
                addChildEquipment( equipmentTypeId );
            }
            result = true;
        }
        return result;
    }

    @Override
    public int getQuantityOfChild(UUID equipmentTypeId) {
        return getQuantityOfChildren().count(equipmentTypeId);
    }

    @Override
    public int getQuantityOfChild(String name) {
        return getQuantityOfChild( NetworkRegistryFactory.getNetworkEquipmentRegistry(this.program).getEquipmentTypeID(name) );
    }

    private void addChildEquipment(Equipment equipment) {
        addChildEquipment( equipment.getEquipmentTypeId() );
    }

    private void addChildEquipment(UUID id) {
        getQuantityOfChildren().add(id);

        this.equipmentNameToCount.put(
                NetworkRegistryFactory.getNetworkEquipmentRegistry(this.program).getEquipmentTypeName(id),
                getQuantityOfChildren().count(id));
    }

    @Override
    public String getUniqueName() {
        return this.uniqueName;
    }

    @Override
    public String getEquipmentTypeName() {
        return this.equipmentTypeName;
    }

    @Override
    public UUID getEquipmentTypeId() {
        return this.equipmentTypeId;
    }

    @Override
    public Multiset<UUID> getQuantityOfChildren() {
        return this.quantityOfChildren;
    }

    @Override
    public String toString() {
        return this.uniqueName;
    }
}
