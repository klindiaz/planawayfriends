import equipment.Equipment;
import executor.EngineerFactory;
import executor.NetworkEngineer;
import init.NetworkEquipmentInitializer;

import java.util.List;

public class EntryPoint {

    public static void main (String[] args) {
        NetworkEquipmentInitializer.init();

        NetworkEngineer networkEngineer = EngineerFactory.getNetworkEngineer("cats");
//        NetworkEngineer networkEngineer = EngineerFactory.getNetworkEngineer("ccap");
//        Equipment equipment = networkEngineer.getEquipmentInstance("chassis");

//        List<Equipment> equipmentList0 = networkEngineer.build("chassis","rpd",723);
//        List<Equipment> equipmentList2 = networkEngineer.build(
//                "chassis",
//                "rpd",
//                8,
//                networkEngineer.build("chassis","rpd",1),
//                false
//                );

//        int rpdCount = 100;
//        List<Equipment> equipmentList3 = networkEngineer.build("chassis","rpd",rpdCount);
//        List<Equipment> equipmentList4 = networkEngineer.build("rpa","rpd",rpdCount);

        List<Equipment> equipmentList = networkEngineer.build(
                "subdivision",
                "cat",
                1,
                networkEngineer.build("subdivision","cat",7)
        );
        System.out.println("DONE");
    }

}
