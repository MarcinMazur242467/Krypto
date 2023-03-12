package krypto.model;

import java.io.*;

public class FileManager {
    private final File fileName;

    public FileManager(File fileName) {
        this.fileName = fileName;
    }

    public Key read() {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            Object obj = objectIn.readObject();
            return (Key) obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(Key key){
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
