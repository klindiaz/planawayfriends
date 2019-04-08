import equipment.Equipment;
import executor.EngineerFactory;
import executor.NetworkEngineer;
import init.NetworkEquipmentInitializer;

import java.util.List;

public class EntryPoint {
    public static void main (String[] args) {
        NetworkEquipmentInitializer.init();

        NetworkEngineer networkEngineer = EngineerFactory.getNetworkEngineer("ccap");
        List<Equipment> equipmentList = networkEngineer.build("chassis","rpd",672);

        System.out.println("DONE");
    }
}
