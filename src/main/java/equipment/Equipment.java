package equipment;

import com.google.common.collect.Multiset;

import java.util.List;
import java.util.Map;
import java.util.UUID;

//TODO: Consider splitting into separate interfaces
public interface Equipment {
//Description
    UUID getEquipmentTypeId();
    String getUniqueName();
    String getEquipmentTypeName();

//Nodes in tree
    Map<UUID,String> getParentTypes();
    Map<UUID,String> getChildrenTypes();
    Map<UUID,List<Equipment>> getChildren();
    Map<UUID, Integer> getChildrenLimits();
    Integer getChildLimit(Equipment equipment);
    Multiset<UUID> getQuantityOfChildren();
    int getQuantityOfChild(UUID equipmentTypeId);
    int getQuantityOfChild(String name);
    long getNumberOfParentTypes();
    long getNumberOfChildrenTypes();

//Population
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
}
