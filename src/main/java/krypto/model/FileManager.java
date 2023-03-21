package krypto.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {
    private final File fileName;

    public FileManager(File fileName) {
        this.fileName = fileName;
    }

    public byte[] read() throws IOException {
        Path path = fileName.toPath();
        return Files.readAllBytes(path);
    }

    public void write(String s) throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(s);
            writer.close();
    }
}


