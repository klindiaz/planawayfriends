package equipment.repository;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.UUID;

public class NetworkEquipmentRepository {
    private static final Multiset<UUID> repo = HashMultiset.create();

    public static int getOccurrences(UUID equipmentTypeId) {
        return repo.count(equipmentTypeId);
    }

    public static void addOccurrence(UUID equipmentTypeId) {
        repo.add(equipmentTypeId);
    }
}
