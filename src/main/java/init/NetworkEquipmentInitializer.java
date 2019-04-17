package init;

import executor.EngineerFactory;
import executor.NetworkEngineer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NetworkEquipmentInitializer {

    public static void init() {

        String filename = "ccap_equipment_configuration.csv";
        NetworkEngineer engineer = EngineerFactory.getNetworkEngineer(filename.split("_")[0]);
        for (Input input : getData(filename)) {
            engineer.addEquipmentAndSpec(input.origin , input.child , input.childToOrigin);
        }
    }

    private static List<Input> getData(final String filename) {
        List<Input> container = new ArrayList<>();
        ClassLoader classLoader = NetworkEquipmentInitializer.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(filename)) {
            Objects.requireNonNull(inputStream);
            List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] elements = line.split(",");
                String root = elements[0];
                String leaf = elements[1];
                String weight = elements[2];

                Objects.requireNonNull(root);
                Objects.requireNonNull(leaf);
                Objects.requireNonNull(weight);
                container.add(new Input(root.trim() , leaf.trim(), Integer.parseInt(weight.trim())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
