package krypto.model;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Key implements Serializable {
    private final List<byte[]> keyList = new ArrayList<>();

    public void addKey(byte[] key){
            keyList.add(key);
    }
    public byte[] getKey(int i){
        return keyList.get(i);
    }

    public List<byte[]> getKeyList() {
        return keyList;
    }

    public void resetKey(){
        keyList.clear();
    }
    // Konwersja tablicy bajtów na łańcuch szesnastkowy
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public void generateKey(){
        // Tworzenie obiektu SecureRandom
        SecureRandom random = new SecureRandom();
        // Generowanie 8 bajtów (64 bitów)
        byte[] key = new byte[8];
        random.nextBytes(key);
        if (key.length > 8) {
            // jeśli liczba jest dłuższa niż 8 bajtów, to obetnij tablicę do pierwszych 8 bajtów
            key = Arrays.copyOf(key, 8);
        }
        addKey(key);
    }
    public void generateSubKey(){
        byte[] mainKey = keyList.get(1);

    }
}
