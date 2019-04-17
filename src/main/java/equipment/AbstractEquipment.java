package equipment;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import equipment.configuration.NetworkDesignFactory;
import equipment.configuration.NetworkEquipmentDesign;
import equipment.configuration.NetworkRegistryFactory;
import equipment.implementation.NetworkEquipment;
import equipment.repository.NetworkEquipmentRepository;
import utility.NameUtil;

import java.util.*;

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

    public AbstractEquipment(AbstractEquipment equipment) {
        this.program = equipment.getProgram();
        this.equipmentTypeName = equipment.getEquipmentTypeName();
        this.equipmentTypeId = equipment.getEquipmentTypeId();
        this.uniqueName = equipment.getUniqueName();

        equipment.getQuantityOfChildren().entrySet().forEach( entry ->  this.quantityOfChildren.add(entry.getElement(),entry.getCount()));
        equipment.getEquipmentNameToCount().forEach( this.equipmentNameToCount::put );
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
    public boolean canAddEquipment(UUID equipmentTypeId , int quantity) {
        int newProposedTotal = getQuantityOfChild(equipmentTypeId) + quantity;
        int max = NetworkDesignFactory
                            .getNetworkEquipmentDesign(this.program)
                            .getMaxQuantityOfChild(this.getEquipmentTypeId() , equipmentTypeId);
        return !isNone() && (newProposedTotal <= max);
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
    public boolean addEquipment(Equipment equipment) {
        return addEquipment(equipment.getEquipmentTypeId() , 1);
    }

    @Override
    public boolean addEquipment(UUID equipmentTypeId) {
        return addEquipment( equipmentTypeId , 1);
    }

    @Override
    public boolean addEquipment(UUID equipmentTypeId , int quantity) {
        boolean result = false;
        if ( equipmentTypeId != NetworkEquipment.NONE.getEquipmentTypeId() && canAddEquipment(equipmentTypeId , quantity) ) {
            addChildEquipment( equipmentTypeId , quantity );
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
        addChildEquipment( equipment.getEquipmentTypeId() , 1);
    }

    private void addChildEquipment(UUID id) {
        addChildEquipment(id , 1);
    }

    private void addChildEquipment(UUID id  ,final int quantity) {
        boolean startingFromScratch = getQuantityOfChildren().size() == 0;
        Map<UUID,Integer> equipmentDesignAndMaximums = NetworkDesignFactory.getNetworkEquipmentDesign(this.program).getBottomUpSpec(this.equipmentTypeId,id);

        Multiset<UUID> resourcesToAdd = !startingFromScratch ?
                                                buildOnExisting(equipmentDesignAndMaximums , id , quantity) :
                                                buildBrandNew(equipmentDesignAndMaximums , id , quantity);
        resourcesToAdd.entrySet().forEach(entry -> addChildEquipmentHelper(entry.getElement(),entry.getCount()));
    }

    private Multiset<UUID> buildOnExisting(final Map<UUID,Integer> bottomUpApproach , final UUID leafID , final int quantity) {
        Multiset<UUID> resourcesToAdd = HashMultiset.create();
        NetworkEquipmentDesign design = NetworkDesignFactory.getNetworkEquipmentDesign(this.program);

        Multiset<UUID> availableResources = HashMultiset.create();
        for (Map.Entry<UUID,Integer> entry : bottomUpApproach.entrySet()) {
            availableResources.add(
                    entry.getKey(),
                    design.getAvailableEquipmentQuantity(this.equipmentTypeId , entry.getKey() , getQuantityOfChild(entry.getKey()))
            );
        }
//        List<UUID> path = new LinkedList<>(bottomUpApproach.keySet());

        UUID childID = leafID;
        UUID parentID;
        for (Map.Entry<UUID,Integer> entry : bottomUpApproach.entrySet()) {
            parentID = entry.getKey();
            int maxNumberOfChildrenInParent = entry.getValue();

            int quantityOfParent = getQuantityOfChild(parentID);
            int numberOfChildrenThatCanFitInParents = (quantityOfParent * maxNumberOfChildrenInParent) - getQuantityOfChild(childID);

            if ( quantity <= numberOfChildrenThatCanFitInParents ) {
                resourcesToAdd.add( childID , quantity );
                break;
            } else {
                int amountRemaining = quantity - numberOfChildrenThatCanFitInParents;
                int newParentsNeeded = (int) Math.ceil((double) amountRemaining / (double) maxNumberOfChildrenInParent);
                resourcesToAdd.add( parentID , newParentsNeeded );
                resourcesToAdd.add( childID , quantity);

                childID = parentID;
            }
        }
        return resourcesToAdd;
    }

    private static Multiset<UUID> buildBrandNew(Map<UUID,Integer> bottomUpApproach , UUID leafID , final int quantity) {
        Multiset<UUID> resourcesToAdd = HashMultiset.create();

        UUID parentID;
        int quantityOfCurrentNode = quantity;
        int maxNumberOfChildrenInParent;

        resourcesToAdd.add(leafID , quantity);
        for (Map.Entry<UUID,Integer> entry : bottomUpApproach.entrySet()) {
            parentID = entry.getKey();
            maxNumberOfChildrenInParent = entry.getValue();

            quantityOfCurrentNode = (int) Math.ceil( (double) quantityOfCurrentNode / (double) maxNumberOfChildrenInParent );
            resourcesToAdd.add(parentID , quantityOfCurrentNode);
        }
        return resourcesToAdd;
    }

    private void addChildEquipmentHelper(UUID id) {
        addChildEquipmentHelper(id , 1);
    }

    private void addChildEquipmentHelper(UUID id , int quantity) {
        getQuantityOfChildren().add(id,quantity);

        this.equipmentNameToCount.put(
                                        NetworkRegistryFactory.getNetworkEquipmentRegistry(this.program).getEquipmentTypeName(id),
                                        getQuantityOfChildren().count(id)
                                    );
    }

    public String getProgram() {
        return program;
    }

    public Map<String, Integer> getEquipmentNameToCount() {
        return Collections.unmodifiableMap(equipmentNameToCount);
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
