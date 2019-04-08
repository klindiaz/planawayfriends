package init;

import executor.EngineerFactory;
import executor.NetworkEngineer;

import java.util.ArrayList;
import java.util.List;

public class NetworkEquipmentInitializer {

    public static void init() {
        List<Input> mockData = getMockCCAPData();

        NetworkEngineer engineer = EngineerFactory.getNetworkEngineer("ccap");

        for (Input input : mockData) {
            engineer.addEquipmentAndSpec(input.origin , input.child , input.childToOrigin);
        }
    }

    private static List<Input> getMockCCAPData() {
        List<Input> container = new ArrayList<>();

        container.add(new Input("chassis" , "line card", 7));
        container.add(new Input("line card","port", 4));
        container.add(new Input("port","nz dsg", 3));
        container.add(new Input("port","dnx dsg", 2));
        container.add(new Input("port","mixed dsg", 2));
        container.add(new Input("nz dsg","rpd", 8));
        container.add(new Input("dnx dsg","rpd", 1));
        container.add(new Input("ocd dsg","rpd", 1));
        container.add(new Input("rpa","rpd", 4));

        return container;
    }

    private static class Input {
        String origin;
        String child;
        int childToOrigin;

        Input(String origin, String child, int childToOrigin) {
            this.origin = origin;
            this.child = child;
            this.childToOrigin = childToOrigin;
        }
    }
}
