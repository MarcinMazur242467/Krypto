package krypto.model;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.*;


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
        permuttedKeyList.clear();
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

    private byte[] rotateLeft28L(byte[] arr) {
        byte temp = (byte) ((arr[0] & 0x80) >> 3);
        shiftLeft(arr, 1);
        arr[3] = (byte) (arr[3] | temp);
        return arr;
    }

    private byte[] rotateLeft28R(byte[] arr) {
        byte temp = (byte) ((arr[0] & 0x8) >> 3);
        shiftLeft(arr, 1);
        arr[3] = (byte) (arr[3] | temp);
        return arr;
    }

    public static byte[] shiftLeft(byte[] byteArray, int shiftBitCount) {
        final int shiftMod = shiftBitCount % 8;
        final byte carryMask = (byte) ((1 << shiftMod) - 1);
        final int offsetBytes = (shiftBitCount / 8);

        int sourceIndex;
        for (int i = 0; i < byteArray.length; i++) {
            sourceIndex = i + offsetBytes;
            if (sourceIndex >= byteArray.length) {
                byteArray[i] = 0;
            } else {
                byte src = byteArray[sourceIndex];
                byte dst = (byte) (src << shiftMod);
                if (sourceIndex + 1 < byteArray.length) {
                    dst |= byteArray[sourceIndex + 1] >>> (8 - shiftMod) & carryMask;
                }
                byteArray[i] = dst;
            }
        }
        return byteArray;
    }

    public void generatePermutedKeys(byte[] key) {
        byte[] permutedKey = permuteFunction(key, PC1Pattern);
        byte[] left = new byte[4];
        byte[] right = new byte[4];
        System.arraycopy(permutedKey, 0, left, 0, 4);
        System.arraycopy(permutedKey, 3, right, 0, 4);
        left[3] = (byte) (permutedKey[3] & 0xf0);
        right[0] = (byte) (permutedKey[3] & 0x0f);
        for (int i = 0; i < 16; i++) {
            if (i == 0 || i == 1 || i == 8 || i == 15) {
                rotateLeft28L(left);
                rotateLeft28R(right);
            } else {
                rotateLeft28L(left);
                rotateLeft28L(left);
                rotateLeft28R(right);
                rotateLeft28R(right);
            }
            byte[] result = new byte[7];
            result[0] = left[0];
            result[1] = left[1];
            result[2] = left[2];
            result[3] = (byte) (left[3] | right[0]);
            result[4] = right[1];
            result[5] = right[2];
            result[6] = right[3];
            result = permuteFunction(result, PC2Pattern);
            permuttedKeyList.add(result);
        }
    }


}
