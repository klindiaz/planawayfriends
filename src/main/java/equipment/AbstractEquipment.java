package equipment;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import equipment.configuration.NetworkEquipmentDesign;
import equipment.configuration.NetworkEquipmentRegistry;
import equipment.implementation.NetworkEquipment;
import equipment.repository.NetworkEquipmentRepository;
import utility.EquipmentNameUtil;

import java.util.*;

public abstract class AbstractEquipment implements Equipment {

    public AbstractEquipment() {
        super();
        this.uniqueName = "NONE";
        this.equipmentTypeName = "NULL OBJECT PATTERN";
        this.equipmentTypeId = UUID.randomUUID();
    }

    public AbstractEquipment(String name) {
        super();
        this.equipmentTypeName = EquipmentNameUtil.getStandardizedName(name);
        this.equipmentTypeId = NetworkEquipmentRegistry.getEquipmentTypeID(this.equipmentTypeName);

        NetworkEquipmentRepository.addOccurrence( this.equipmentTypeId );
        this.uniqueName = this.equipmentTypeName + " - " + NetworkEquipmentRepository.getOccurrences(this.equipmentTypeId);
    }

    private final String uniqueName;
    private final UUID equipmentTypeId;
    private final String equipmentTypeName;
    private final Map<UUID, List<Equipment>> children = new HashMap<>();
    private final Multiset<UUID> quantityOfChildren = HashMultiset.create();

    public boolean isFull(UUID equipmentType) {
        return !canAddEquipment(equipmentType);
    }

    public boolean canAddEquipment(UUID equipmentType) {
        return canAddEquipment(equipmentType , 1);
    }

    public boolean canAddEquipment(Equipment equipment) {
        return canAddEquipment(equipment , 1);
    }

    public boolean canAddEquipment(Equipment equipment , int quantity) {
        return canAddEquipment(equipment.getEquipmentTypeId() , quantity);
    }


    public boolean addEquipment(String name) {
        return addEquipment( name , 1 );
    }

    public boolean addEquipment(String name , int quantity) {
        return addEquipment(
                NetworkEquipmentRegistry.getEquipmentTypeID(name),
                quantity
        );
    }


    public boolean canAddEquipment(UUID equipmentTypeId , int quantity) {
        return (getQuantityOfChild(equipmentTypeId) + quantity) <= NetworkEquipmentDesign.getMaxQuantityOfChild(this.getEquipmentTypeId() , equipmentTypeId);
    }

    public boolean addEquipment(Equipment equipment) {
        boolean result = false;
        if (!equipment.isNone() && canAddEquipment(equipment)) {
            addChildEquipment(equipment);
            result = true;
        }
        return result;
    }

    public boolean addEquipment(UUID equipmentTypeId) {
        return addEquipment( equipmentTypeId , 1);
    }

    public boolean addEquipment(UUID equipmentTypeId , int quantity) {
        boolean result = false;
        if ( equipmentTypeId != NetworkEquipment.NONE.getEquipmentTypeId() && canAddEquipment(equipmentTypeId , quantity) ) {
            for (int i = 0 ; i < quantity ; i++) {
                addChildEquipment( NetworkEquipmentRegistry.getEquipmentInstance(equipmentTypeId) );
            }
            result = true;
        }
        return result;
    }

    public int getQuantityOfChild(UUID equipmentTypeId) {
        return getQuantityOfChildren().count(equipmentTypeId);
    }

    public int getQuantityOfChild(String name) {
        return getQuantityOfChild( NetworkEquipmentRegistry.getEquipmentTypeID(name) );
    }

    public long getNumberOfParentTypes() {
        return getParentTypes() != null ? getParentTypes().size() : 0;
    }

    public long getNumberOfChildrenTypes() {
        return getChildrenTypes() != null ? getChildrenTypes().size() : 0;
    }


    private void addChildEquipment(Equipment child) {
        List<Equipment> list = getChildren().getOrDefault(child.getEquipmentTypeId() , new ArrayList<>());
        list.add( child );
        getChildren().put(child.getEquipmentTypeId() , list);
        getQuantityOfChildren().add(child.getEquipmentTypeId());
    }

    @Override
    public Map<UUID, List<Equipment>> getChildren() {
        return this.children;
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
