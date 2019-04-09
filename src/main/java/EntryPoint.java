import equipment.Equipment;
import executor.EngineerFactory;
import executor.NetworkEngineer;
import init.NetworkEquipmentInitializer;

import java.util.List;

public class EntryPoint {
    public static void main (String[] args) {
        NetworkEquipmentInitializer.init();

        NetworkEngineer networkEngineer = EngineerFactory.getNetworkEngineer("ccap");
        List<Equipment> equipmentList1 = networkEngineer.build("chassis","rpd",671);
        List<Equipment> equipmentList2 = networkEngineer.build("chassis","rpd",1 , equipmentList1);

        System.out.println("DONE");
    }
}