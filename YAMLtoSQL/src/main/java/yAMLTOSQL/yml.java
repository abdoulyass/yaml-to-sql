package yAMLTOSQL;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Map;
import java.io.InputStream;
import org.yaml.snakeyaml.Yaml;
import java.awt.Font;

public class yml extends JFrame {

    private JTextField yamlFilePathField;

    public yml() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 584, 304);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        JLabel lblYamlFile = new JLabel("Chemin du fichier YAML :");
        lblYamlFile.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblYamlFile.setBounds(45, 99, 200, 20);
        contentPane.add(lblYamlFile);

        yamlFilePathField = new JTextField();
        yamlFilePathField.setBounds(197, 94, 250, 30);
        contentPane.add(yamlFilePathField);

        JButton btnChooseYamlFile = new JButton("browse");
        btnChooseYamlFile.setBounds(456, 94, 102, 30);
        contentPane.add(btnChooseYamlFile);

        JButton btnConvert = new JButton("Convertir en SQL");
        btnConvert.setBounds(253, 148, 150, 30);
        contentPane.add(btnConvert);

        btnChooseYamlFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    yamlFilePathField.setText(selectedFile.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun fichier sélectionné.");
                }
            }
        });

        btnConvert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String yamlFilePath = yamlFilePathField.getText();
                if (yamlFilePath.endsWith(".yaml")) {
                    convertYAMLtoSQL(yamlFilePath);
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un fichier YAML valide.");
                }
            }
        });
    }

    public static void convertYAMLtoSQL(String yamlFilePath) {
        try {
            Yaml yaml = new Yaml();
            Map<String, Map<String, Object>> yamlData;
            try (InputStream inputStream = new FileInputStream(yamlFilePath)) {
                yamlData = yaml.load(inputStream);
            }

            if (yamlData != null) {
                String outputSqlFilePath = yamlFilePath.replace(".yaml", ".sql");
                try (FileWriter writer = new FileWriter(outputSqlFilePath)) {
                    for (Map.Entry<String, Map<String, Object>> entry : yamlData.entrySet()) {
                        String tableName = entry.getKey();
                        Map<String, Object> data = entry.getValue();

                        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
                        StringBuilder values = new StringBuilder(" VALUES (");

                        for (Map.Entry<String, Object> column : data.entrySet()) {
                            sql.append(column.getKey()).append(", ");
                            values.append("'").append(column.getValue()).append("', ");
                        }

                        sql.delete(sql.length() - 2, sql.length());
                        values.delete(values.length() - 2, values.length());

                        sql.append(")").append(values).append(");");

                        writer.write(sql.toString() + "\n");
                    }
                }
                JOptionPane.showMessageDialog(null, "Conversion terminée ! Fichier SQL généré : " + outputSqlFilePath);
            }  else {
                JOptionPane.showMessageDialog(null, "Le fichier YAML est vide ou invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Une erreur s'est produite : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
               yml frame = new yml();
             
               frame.setLocationByPlatform(true);
                frame.setVisible(true);
               
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
