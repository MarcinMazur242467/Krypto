package krypto.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

import static krypto.model.DESX.test;

public class Key implements Serializable {
    private final List<byte[]> keyList = new ArrayList<>();
    private static final List<byte[]> permuttedKeyList = new ArrayList<>();

    byte[] PC1Pattern = new byte[]{
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    byte[] PC2Pattern = new byte[]{
            14, 17, 11, 24, 1, 5, 3, 28,
            15, 6, 21, 10, 23, 19, 12, 4,
            26, 8, 16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40,
            51, 45, 33, 48, 44, 49, 39, 56,
            34, 53, 46, 42, 50, 36, 29, 32
    };


    public void addKey(byte[] key) {
        keyList.add(key);
    }

    public byte[] getKey(int i) {
        return keyList.get(i);
    }

    public static byte[] getPermuttedKey(int i) {
        return Arrays.copyOfRange(permuttedKeyList.get(i), 0, 6);
    }

    public void resetKey() {
        keyList.clear();
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public void generateKey() {
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

    public byte[] permuteFunction(byte[] block, byte[] permutationPattern) {
        byte[] output = new byte[permutationPattern.length / 8];
        for (int i = 0; i < permutationPattern.length; i++) {
            int index = permutationPattern[i] - 1; // subtract 1 to adjust for 0-based indexing
            int byteIndex = index / 8; // calculate the index of the byte that contains the bit
            int bitIndex = 7 - (index % 8); // calculate the index of the bit within the byte
            byte bit = (byte) ((block[byteIndex] >> bitIndex) & 0x01); // extract the bit at the specified position
            output[i / 8] |= (bit << (7 - (i % 8))); // set the bit in the corresponding position in the permuted key
        }
        return output;
    }

    public byte[] bitRotateLeftByOne(byte[] table) {
        BigInteger shitfR = new BigInteger(table);
        BigInteger shitfL = new BigInteger(table);
        shitfR = shitfR.shiftRight(27);
        shitfL = shitfL.shiftLeft(1);
        return shitfR.or(shitfL).toByteArray();
    }

    public byte[] bitRotateLeftByTwo(byte[] table) {
        BigInteger shitfR = new BigInteger(table);
        BigInteger shitfL = new BigInteger(table);
        shitfR = shitfR.shiftRight(26);
        shitfL = shitfL.shiftLeft(2);
        return shitfR.or(shitfL).toByteArray();
    }

    public void generatePermutedKeys(byte[] key) {
//        test(key);
//        key = new byte[key.length - 1];
//        System.arraycopy(key, 1, key, 0, key.length - 1);
//        test(key);
        //  generalnie ten klucz jest 64 bit z czego 1 byte to same 0; niby w prezentacji jest napisane:[...]nastépnie z pozostaych 56 bitow tworzony jest 16 podkluczy[...]"
        byte[] permutedKey = permuteFunction(key, PC1Pattern);
        BigInteger bigNum = new BigInteger(1, permutedKey);
        byte[] rightKey = bigNum.shiftRight(28).and(new BigInteger("FFFFFFF", 16)).toByteArray();
        byte[] leftKey = bigNum.and(new BigInteger("FFFFFFF", 16)).toByteArray();
        for (int i = 0; i < 16; i++) {
            if (i == 0 || i == 1 || i == 8 || i == 15) {
                rightKey = bitRotateLeftByOne(rightKey);
                leftKey = bitRotateLeftByOne(leftKey);
            } else {
                rightKey = bitRotateLeftByTwo(rightKey);
                leftKey = bitRotateLeftByTwo(leftKey);
            }
            BigInteger bigIntegerLeft = new BigInteger(1, rightKey);
            BigInteger bigIntegerRight = new BigInteger(1, leftKey);
            BigInteger bigInteger = bigIntegerRight.shiftLeft(28).or(bigIntegerLeft);
            permutedKey = bigInteger.toByteArray();

            permutedKey = permuteFunction(permutedKey, PC2Pattern);

            permuttedKeyList.add(permutedKey);
        }
    }
}
