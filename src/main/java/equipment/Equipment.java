package equipment;

import com.google.common.collect.Multiset;

import java.util.UUID;

public interface Equipment {
    UUID getEquipmentTypeId();
    String getUniqueName();
    String getEquipmentTypeName();

    Multiset<UUID> getQuantityOfChildren();
    int getQuantityOfChild(UUID equipmentTypeId);
    int getQuantityOfChild(String name);

    boolean isNone();
    boolean isFull(UUID equipmentType);
    boolean canAddEquipment(UUID equipmentType);
    boolean canAddEquipment(Equipment equipment);
    boolean canAddEquipment(Equipment equipment , int quantity);
    boolean canAddEquipment(UUID equipmentTypeId , int quantity);
    boolean addEquipment(String name);
    boolean addEquipment(String name , int quantity);
    boolean addEquipment(Equipment equipment);
    boolean addEquipment(UUID equipmentTypeId);
    boolean addEquipment(UUID equipmentTypeId , int quantity);

    Equipment getClone();
}
