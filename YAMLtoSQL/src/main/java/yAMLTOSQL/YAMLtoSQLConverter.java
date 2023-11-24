package yAMLTOSQL;

import org.yaml.snakeyaml.Yaml;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
public class YAMLtoSQLConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the path to the YAML file: ");
        String yamlFilePath = scanner.nextLine();

        System.out.print("Enter the path for the output SQL file: ");
        String outputSqlFile = scanner.nextLine();

        try {
            // Load the YAML data into a map
            Yaml yaml = new Yaml();
            Map<String, Map<String, Object>> yamlData;
            try (InputStream inputStream = new FileInputStream(yamlFilePath)) {
                yamlData = yaml.load(inputStream);
            }

            // Create FileWriter
            try (FileWriter writer = new FileWriter(outputSqlFile)) {
                if (yamlData != null) {
                    for (Map.Entry<String, Map<String, Object>> entry : yamlData.entrySet()) {
                        String tableName = entry.getKey();
                        Map<String, Object> data = entry.getValue();

                        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
                        StringBuilder values = new StringBuilder(" VALUES (");

                        for (Map.Entry<String, Object> column : data.entrySet()) {
                            sql.append(column.getKey()).append(", ");
                            values.append("'").append(column.getValue()).append("', ");
                        }

                        // Remove the trailing comma
                        sql.delete(sql.length() - 2, sql.length());
                        values.delete(values.length() - 2, values.length());

                        sql.append(")").append(values).append(");");

                        writer.write(sql.toString() + "\n");
                    }
                } else {
                    System.out.println("YAML file is empty or invalid.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}