package equipment.implementation;

import equipment.AbstractEquipment;
import equipment.Equipment;
import equipment.configuration.NetworkEquipmentDesign;
import equipment.configuration.NetworkEquipmentRegistry;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NetworkEquipment extends AbstractEquipment {
    public static final NetworkEquipment NONE = new NetworkEquipment();

    private NetworkEquipment() {
        super();
    }

    public NetworkEquipment(String name) {
        super(name);
    }

    @Override
    public boolean isNone() {
        return this == NONE;
    }

    @Override
    public Map<UUID,String> getParentTypes() {
        return NetworkEquipmentDesign
                        .getParentTypes(this.getEquipmentTypeId())
                        .stream()
                        .collect(
                                Collectors.toMap(Function.identity() , NetworkEquipmentRegistry::getEquipmentTypeName)
                        );
    }

    @Override
    public Map<UUID,String> getChildrenTypes() {
        return NetworkEquipmentDesign
                        .getChildrenTypes(this.getEquipmentTypeId())
                        .stream()
                        .collect(
                                Collectors.toMap(Function.identity() , NetworkEquipmentRegistry::getEquipmentTypeName)
                        );
    }

    @Override
    public Map<UUID, Integer> getChildrenLimits() {
        return NetworkEquipmentDesign.getChildrenLimits(this.getEquipmentTypeId());
    }

    @Override
    public Integer getChildLimit(Equipment equipment) {
        return NetworkEquipmentDesign.getMaxQuantityOfChild(this.getEquipmentTypeId() , equipment.getEquipmentTypeId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEquipment that = (AbstractEquipment) o;
        return Objects.equals(this.getEquipmentTypeId(), that.getEquipmentTypeId()) &&
                Objects.equals(this.getEquipmentTypeName(), that.getEquipmentTypeName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEquipmentTypeName(), this.getEquipmentTypeId());
    }
}
