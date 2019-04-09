package equipment.implementation;

import equipment.AbstractEquipment;

import java.util.Objects;
import java.util.UUID;

public class NetworkEquipment extends AbstractEquipment {
    public static final NetworkEquipment NONE = new NetworkEquipment();

    private NetworkEquipment() {
        super();
    }

    private NetworkEquipment(NetworkEquipment equipment) {
        super(equipment);
    }

    NetworkEquipment(String program , String name , UUID id) {
        super(program , name , id);
    }

    @Override
    public NetworkEquipment getClone() {
        return new NetworkEquipment(this);
    }

    @Override
    public boolean isNone() {
        return this == NONE;
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
