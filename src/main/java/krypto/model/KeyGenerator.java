package krypto.model;

import java.util.Random;

public class KeyGenerator{
    public String generateKey(){
        String alphabet = "0123456789abcdefABCDEF";
        StringBuilder sb = new StringBuilder(16);
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
