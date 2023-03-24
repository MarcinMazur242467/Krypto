package krypto.model;


import java.io.*;

public class FileObjManager {
    private final File fileName;

    public FileObjManager(File fileName) {
        this.fileName = fileName;
    }

    public <T> T read() {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            Object obj = objectIn.readObject();
            return (T) obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void write(T t) {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(t);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
