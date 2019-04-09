package executor;

import equipment.Equipment;
import equipment.configuration.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class NetworkEngineer {

    NetworkEngineer(final String program) {
        this.program = program;
        this.registry = NetworkRegistryFactory.createNetworkEquipmentRegistry(program);
        this.design = NetworkDesignFactory.createNetworkEquipmentDesign(program);
    }

    private final String program;
    private final NetworkEquipmentRegistry registry;
    private final NetworkEquipmentDesign design;

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
        List<Equipment> equipmentList = new ArrayList<>(inputEquipmentList);

        int maxNumberOfChildren = this.design.getMaxQuantityOfChild(rootID , nthChildID);
        int numberToAccountFor = numberOfChildren;
        while (numberToAccountFor > 0) {
            int accountFor = numberToAccountFor > maxNumberOfChildren ? maxNumberOfChildren : numberToAccountFor;
            List<Equipment> candidates = equipmentList
                                            .stream()
                                            .filter( equipment -> equipment.getEquipmentTypeId() == rootID && equipment.canAddEquipment(nthChildID,accountFor) )
                                            .collect(Collectors.toList());
            Equipment equipment;
            if (candidates.isEmpty()) {
                equipment = NetworkEquipmentFactory.getEquipmentInstance(this.program,rootID);
                equipmentList.add( equipment );
                continue;
            } else {
                equipment = candidates.get(0);
                equipment.addEquipment(nthChildID , accountFor);
            }
            numberToAccountFor -= accountFor;
        }
        return equipmentList;
    }

    public String getProgram() {
        return this.program;
    }

    private NetworkEngineer addEquipment(String equipmentTypeName) {
        this.getProgramRegistry().addEquipmentToRegistry(equipmentTypeName);

        return this;
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
}
