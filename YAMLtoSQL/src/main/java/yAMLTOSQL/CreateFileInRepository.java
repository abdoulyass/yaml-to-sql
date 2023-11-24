package yAMLTOSQL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateFileInRepository {
    public static void main(String[] args) {
        // Specify the repository (directory) where you want to create the file
        String repositoryPath = "yamal//";

        // Specify the filename you want to create
        String fileName = "example.txt";

        // Combine the repository path and filename to get the full file path
        String filePath = repositoryPath + "/" + fileName;

        // Use Paths to create a Path object from the combined path
        Path path = Paths.get(filePath);

        // Create the file
        try {
            Files.createFile(path);
            System.out.println("File created successfully at: " + path.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
