package krypto.model;

import java.io.*;

public class FileManager {
    private final File fileName;

    public FileManager(File fileName) {
        this.fileName = fileName;
    }


    public byte[] read() {
        File file = new File(fileName.getPath());
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream input = new FileInputStream(file)) {
            input.read(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }


    public void write(byte[] text) throws IOException{
        File file = new File(fileName.getPath());
        file.createNewFile();
        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

