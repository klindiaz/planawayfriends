package equipment.configuration;

import com.google.common.collect.ImmutableMap;
import equipment.implementation.NetworkEquipment;
import org.apache.commons.collections4.CollectionUtils;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.*;
import java.util.stream.Collectors;

public class NetworkEquipmentDesign {

    NetworkEquipmentDesign(String program) {
        this.program = program;
    }

    private final String program;
    private final Map<UUID,List<UUID>> equipmentParents = new HashMap<>();
    private final Map<UUID,List<UUID>> equipmentChildren = new HashMap<>();
    private final Map<UUID,Map<UUID,Integer>> childrenLimits = new HashMap<>();   // < parentUUID , < childUUID , max > >
    private final DirectedGraph<UUID, EquipmentEdge> graph = new DefaultDirectedGraph<>(EquipmentEdge.class);
    private final DijkstraShortestPath<UUID,EquipmentEdge> path = new DijkstraShortestPath<>(graph);


    public List<UUID> getParentTypes(UUID equipmentTypeId) {
        return equipmentParents.getOrDefault(equipmentTypeId , Collections.singletonList(NetworkEquipment.NONE.getEquipmentTypeId()));
    }

    public List<UUID> getChildrenTypes(UUID equipmentTypeId) {
        return equipmentChildren.getOrDefault(equipmentTypeId , Collections.singletonList(NetworkEquipment.NONE.getEquipmentTypeId()));
    }

    public Map<UUID, Integer> getChildrenLimits(UUID equipmentTypeId) {
        return childrenLimits.getOrDefault(equipmentTypeId , Collections.singletonMap(NetworkEquipment.NONE.getEquipmentTypeId() , 0));
    }

    public int getMaxQuantityOfChild(UUID origin , UUID child) {
        return path.getPath(origin,child).getEdgeList().stream().map(EquipmentEdge::getValue).reduce(Math::multiplyExact).orElse(0);
    }


    public List<EquipmentEdge> getEdgeList(UUID rootId , UUID leafNodeID) {
        return new ArrayList<>(
                            this.path
                                .getPath(rootId,leafNodeID)
                                .getEdgeList()
                    );
    }

    public Map<UUID,Integer> getDesignMultipliers(UUID rootId , UUID leafNodeID) {
        Map<UUID,Integer> result = new LinkedHashMap<>();

        List<EquipmentEdge> edges = getEdgeList(rootId,leafNodeID);
        for (int i = edges.size() - 1 ; i >= 0 ; i--) {
            EquipmentEdge edge = edges.get(i);
            result.put(edge.origin , edge.value);
        }
        return result;
    }

    public void addParentEquipmentType(UUID origin , UUID parent) {
        addEquipmentTypeToHierarchy(origin , parent , equipmentParents);
    }

    public void addChildEquipment(UUID origin , UUID child , int maxNumberOfChildrenForParent) {
        addEquipmentTypeToHierarchy(origin , child , equipmentChildren);
        childrenLimits.put(origin , ImmutableMap.of( child , maxNumberOfChildrenForParent) );

        graph.addEdge(origin,child, new EquipmentEdge(origin , child , maxNumberOfChildrenForParent));
    }

    private void addEquipmentTypeToHierarchy(UUID origin , UUID destination , Map<UUID,List<UUID>> container) {
        List<UUID> subContainer = !CollectionUtils.isEmpty(container.get(origin)) ? container.get(origin) : new ArrayList<>();

        subContainer.add(destination);
        container.put( origin , subContainer );

        graph.addVertex(origin);
        graph.addVertex(destination);
    }

    private final class EquipmentEdge {
        final UUID origin;
        final UUID destination;
        final Integer value;

        EquipmentEdge(UUID origin, UUID destination, Integer value) {
            this.origin = origin;
            this.destination = destination;
            this.value = value;
        }

        int getValue(){
            return this.value;
        }

        public UUID getOrigin() {
            return origin;
        }

        public UUID getDestination() {
            return destination;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EquipmentEdge that = (EquipmentEdge) o;
            return Objects.equals(origin, that.origin) &&
                    Objects.equals(destination, that.destination) &&
                    Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(origin, destination, value);
        }

        @Override
        public String toString() {
            return String.format(
                    "%s:%s - 1:%s",
                    NetworkRegistryFactory.getNetworkEquipmentRegistry(program).getEquipmentTypeName(this.origin),
                    NetworkRegistryFactory.getNetworkEquipmentRegistry(program).getEquipmentTypeName(this.destination),
                    this.value
            );
        }
    }
}
