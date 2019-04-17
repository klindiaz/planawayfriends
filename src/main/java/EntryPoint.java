import equipment.Equipment;
import executor.EngineerFactory;
import executor.NetworkEngineer;
import init.NetworkEquipmentInitializer;

import java.util.List;

public class EntryPoint {

    public static void main (String[] args) {
        NetworkEquipmentInitializer.init();

        NetworkEngineer networkEngineer = EngineerFactory.getNetworkEngineer("ccap");
//        Equipment equipment = networkEngineer.getEquipmentInstance("chassis");

        List<Equipment> equipmentList0 = networkEngineer.build("chassis","rpd",723);
//        List<Equipment> equipmentList2 = networkEngineer.build("chassis","rpd",10 ,
//                networkEngineer.build("chassis","rpd",723)
//                );

        System.out.println("DONE");
    }

}
