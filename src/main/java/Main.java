import equipment.configuration.NetworkEquipmentDesign;
import equipment.configuration.NetworkEquipmentRegistry;
import init.NetworkEquipmentInitializer;

import java.util.UUID;

public class Main {
    public static void main (String[] args) {
        NetworkEquipmentInitializer.init();

//        Equipment chassis = NetworkEquipmentRegistry.getEquipmentInstance("chassis");
//        UUID lineCard = NetworkEquipmentRegistry.getEquipmentTypeID("line card");
//        UUID port = NetworkEquipmentRegistry.getEquipmentTypeID("port");
//
//        System.out.println( "Can add 6 line cards to chassis = "+chassis.canAddEquipment( lineCard , 6) );
//        chassis.addEquipment( lineCard , 6 );
//        System.out.println( "Is Chassis full = "+chassis.isFull( lineCard ) );
//        Equipment anotherLC = NetworkEquipmentRegistry.getEquipmentInstance("line card");
//        System.out.println( "Can add another line card = "+chassis.addEquipment( anotherLC ) );
//        System.out.println( "Is Chassis full = "+chassis.isFull( lineCard ) );
//        System.out.println( "There are how many LC in chassis ... " +chassis.getQuantityOfChild(lineCard) );
//
//        anotherLC.addEquipment(port);


        UUID chassis = NetworkEquipmentRegistry.getEquipmentTypeID("chassis");
        UUID port = NetworkEquipmentRegistry.getEquipmentTypeID("port");
        System.out.println( NetworkEquipmentDesign.getMaxQuantityOfChild(chassis , port) );

        UUID rpa = NetworkEquipmentRegistry.getEquipmentTypeID("rpa");
        UUID rpd = NetworkEquipmentRegistry.getEquipmentTypeID("rpd");
        System.out.println( NetworkEquipmentDesign.getMaxQuantityOfChild(rpa , rpd) );

        System.out.println("DONE");
        /*
            Configure equipment.Equipment

            Populate a chassis with RPDs until full

         */
    }
}
