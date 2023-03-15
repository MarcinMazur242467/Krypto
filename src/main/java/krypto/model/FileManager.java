package krypto.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {
    private final File fileName;

    public FileManager(File fileName) {
        this.fileName = fileName;
    }

    public List<String> read() {
        List<String> list = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(fileName);

            while (scanner.hasNextLine()) {
                list.add(scanner.nextLine());
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void write(String s) throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(s);
            writer.close();
    }
}


