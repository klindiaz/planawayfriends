package executor;

import equipment.Equipment;
import equipment.configuration.NetworkDesignFactory;
import equipment.configuration.NetworkEquipmentDesign;
import equipment.configuration.NetworkEquipmentRegistry;
import equipment.configuration.NetworkRegistryFactory;
import equipment.implementation.NetworkEquipmentFactory;
import exception.ExceedsEquipmentLimit;
import utility.ErrorsUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class NetworkEngineer {

    private final String program;
    private final NetworkEquipmentRegistry registry;
    private final NetworkEquipmentDesign design;

    NetworkEngineer(final String program) {
        this.program = program;
        this.registry = NetworkRegistryFactory.createNetworkEquipmentRegistry(program);
        this.design = NetworkDesignFactory.createNetworkEquipmentDesign(program);
    }

    public List<Equipment> build(String rootName , String nthChildName , int numberOfChildren) {
        return build(rootName , nthChildName , numberOfChildren , new ArrayList<>());
    }

    public List<Equipment> build(String rootName , String nthChildName , int numberOfChildren , final List<Equipment> inputEquipmentList) {
        UUID rootID = this.registry.getEquipmentTypeID(rootName);
        UUID childID = this.registry.getEquipmentTypeID(nthChildName);

        return build(rootID , childID , numberOfChildren , inputEquipmentList);
    }

    public List<Equipment> build(final UUID rootID , final UUID nthChildID , final int numberOfChildren) {
        return build(rootID,nthChildID,numberOfChildren, new ArrayList<>());
    }

    public List<Equipment> build(final UUID rootID , final UUID nthChildID , final int numberOfChildren , final List<Equipment> inputEquipmentList) {
        return build(rootID , nthChildID , numberOfChildren , inputEquipmentList , true);
    }

    public List<Equipment> build(
                                    final UUID rootID ,
                                    final UUID nthChildID ,
                                    final int numberOfChildren ,
                                    final List<Equipment> inputEquipmentList ,
                                    final boolean canSeparateChildren
                            ) throws ExceedsEquipmentLimit
    {
        List<Equipment> equipmentList = inputEquipmentList.stream().map(Equipment::getClone).collect(Collectors.toList());

        int maxNumberOfChildren = this.design.getMaxQuantityOfChild(rootID , nthChildID);
        int numberToAccountFor = numberOfChildren;
        if ((numberToAccountFor > maxNumberOfChildren) && !canSeparateChildren) {
            throw new ExceedsEquipmentLimit(
                    ErrorsUtil.equipmentLimitationExceeded(this.registry.getEquipmentTypeName(rootID),this.registry.getEquipmentTypeName(nthChildID),numberOfChildren,maxNumberOfChildren)
            );
        }
        while (numberToAccountFor > 0) {
            int accountFor = numberToAccountFor > maxNumberOfChildren ? maxNumberOfChildren : numberToAccountFor;
            List<Equipment> candidates = equipmentList
                                            .stream()
                                            .filter( equipment -> equipment.getEquipmentTypeId() == rootID && equipment.canAddEquipment(nthChildID,accountFor) )
                                            .sorted(Comparator.comparing(equipment -> equipment.getQuantityOfChild(nthChildID), Comparator.reverseOrder()))
                                            .collect(Collectors.toList());
            Equipment equipment;
            if ( !candidates.isEmpty() ) {
                Equipment mostDenselyPopulatedCandidate = candidates.get(0);
                mostDenselyPopulatedCandidate.addEquipment(nthChildID , accountFor);

                numberToAccountFor -= accountFor;
            } else {
                equipment = NetworkEquipmentFactory.getEquipmentInstance(this.program,rootID);
                equipmentList.add( equipment );
            }
        }
        return equipmentList;
    }

    public NetworkEngineer addEquipmentAndSpec(String rootName , String childName , int rootToChildRatio) {
        this.addEquipment(rootName)
                .addEquipment(childName)
                .getProgramDesign()
                .addChildEquipment(
                    this.getProgramRegistry().getEquipmentTypeID(rootName),
                    this.getProgramRegistry().getEquipmentTypeID(childName),
                    rootToChildRatio
                );
        return this;
    }

    public NetworkEquipmentRegistry getProgramRegistry() {
        return this.registry;
    }

    public NetworkEquipmentDesign getProgramDesign() {
        return this.design;
    }

    public String getProgram() {
        return this.program;
    }

    public Equipment getEquipmentInstance(final String equipmentTypeName) {
        return NetworkEquipmentFactory.getEquipmentInstance(this.program , equipmentTypeName);
    }

    private NetworkEngineer addEquipment(String equipmentTypeName) {
        this.getProgramRegistry().addEquipmentToRegistry(equipmentTypeName);

        return this;
    }

}
