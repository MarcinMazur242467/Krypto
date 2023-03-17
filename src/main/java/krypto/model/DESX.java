package krypto.model;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DESX {

    private static final int[][] S_BOXES = {
            // S-box 1
            {
                    14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
                    0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
                    4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
                    15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13
            },
            // S-box 2
            {
                    15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
                    3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
                    0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
                    13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9
            },
            // S-box 3
            {
                    10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
                    13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
                    13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
                    1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12
            },// S-box 4
            {
                    7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
                    13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
                    10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
                    3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14
            },
// S-box 5
            {
                    2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
                    14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
                    4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
                    11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3
            },
// S-box 6
            {
                    12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
                    10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
                    9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
                    4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13
            },
            // S-box 7
            {
                    4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
                    13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
                    1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
                    6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12
            },

            // S-box 8
            {
                    13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
                    1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
                    7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
                    2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11
            }
    };

    public static byte[][] divideIntoBlocks(String text) {
        int counter = 0;
        byte[] bytes = text.getBytes();
        int blockCount = (int) Math.ceil((double) bytes.length / 8);
        byte[][] blocks = new byte[blockCount + 1][8];
        int index = 0;
        for (int i = 0; i < blockCount; i++) {
            for (int j = 0; j < 8; j++) {
                if (index < bytes.length) {
                    blocks[i][j] = bytes[index];
                    index++;
                } else {
                    blocks[i][j] = 0;
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            if (blockCount > 0) {
                if (blocks[blockCount - 1][i] == 0) {
                    counter++;
                }
            }
        }
        blocks[blockCount] = ByteBuffer.allocate(4).putInt(counter).array();


        return blocks;
    }

    private byte[] PC1(byte[] key) {
        byte[] permutedKey = new byte[7];
        byte[] PC1_TABLE = new byte[]{
                57, 49, 41, 33, 25, 17, 9,
                1, 58, 50, 42, 34, 26, 18,
                10, 2, 59, 51, 43, 35, 27,
                19, 11, 3, 60, 52, 44, 36,
                63, 55, 47, 39, 31, 23, 15,
                7, 62, 54, 46, 38, 30, 22,
                14, 6, 61, 53, 45, 37, 29,
                21, 13, 5, 28, 20, 12, 4
        };
        for (int i = 0; i < PC1_TABLE.length; i++) {
            byte index = (byte) (PC1_TABLE[i] - 1); // subtract 1 to adjust for 0-based indexing
            byte byteIndex = (byte) (index / 8); // calculate the index of the byte that contains the bit
            byte bitIndex = (byte) (7 - (index % 8)); // calculate the index of the bit within the byte
            byte bit = (byte) ((key[byteIndex] >> bitIndex) & 0x01); // extract the bit at the specified position
            permutedKey[i / 8] |= (bit << (7 - (i % 8))); // set the bit in the corresponding position in the permuted key
        }
        return permutedKey;
    }

    private byte[] PC2(byte[] key) {
        byte[] permutedKey = new byte[7];
        byte[] PC1_TABLE = new byte[]{
                14, 17, 11, 24, 1, 5, 3, 28,
                15, 6, 21, 10, 23, 19, 12, 4,
                26, 8, 16, 7, 27, 20, 13, 2,
                41, 52, 31, 37, 47, 55, 30, 40,
                51, 45, 33, 48, 44, 49, 39, 56,
                34, 53, 46, 42, 50, 36, 29, 32
        };
        for (int i = 0; i < PC1_TABLE.length; i++) {
            byte index = (byte) (PC1_TABLE[i] - 1); // subtract 1 to adjust for 0-based indexing
            byte byteIndex = (byte) (index / 8); // calculate the index of the byte that contains the bit
            byte bitIndex = (byte) (7 - (index % 8)); // calculate the index of the bit within the byte
            byte bit = (byte) ((key[byteIndex] >> bitIndex) & 0x01); // extract the bit at the specified position
            permutedKey[i / 8] |= (bit << (7 - (i % 8))); // set the bit in the corresponding position in the permuted key
        }
        return permutedKey;
    }

    public byte[] initialPermutation(byte[] block) {
        byte[] permutedBock = new byte[8];
        byte[] initialPermutationTable = new byte[]{
                58, 50, 42, 34, 26, 18, 10, 2,
                60, 52, 44, 36, 28, 20, 12, 4,
                62, 54, 46, 38, 30, 22, 14, 6,
                64, 56, 48, 40, 32, 24, 16, 8,
                57, 49, 41, 33, 25, 17, 9, 1,
                59, 51, 43, 35, 27, 19, 11, 3,
                61, 53, 45, 37, 29, 21, 13, 5,
                63, 55, 47, 39, 31, 23, 15, 7
        };

        // TO NIE BEDZIE DZIALAC
        // TO NIE BEDZIE DZIALAC
        // TO NIE BEDZIE DZIALAC
        // TO NIE BEDZIE DZIALAC
        // TO NIE BEDZIE DZIALAC
        for (int i = 0; i < initialPermutationTable.length; i++) {
            byte index = (byte) (initialPermutationTable[i] - 1); // subtract 1 to adjust for 0-based indexing
            byte byteIndex = (byte) (index / 8); // calculate the index of the byte that contains the bit
            byte bitIndex = (byte) (7 - (index % 8)); // calculate the index of the bit within the byte
            byte bit = (byte) ((block[byteIndex] >> bitIndex) & 0x01); // extract the bit at the specified position
            permutedBock[i / 8] |= (bit << (7 - (i % 8))); // set the bit in the corresponding position in the permuted key
        }
        return permutedBock;
    }

    public byte[] extendedPermutation(byte[] block) {
        byte[] result = new byte[6];
        byte[] extendingPermutation = new byte[]{
                32, 1, 2, 3, 4, 5, 4, 5,
                6, 7, 8, 9, 8, 9, 10, 11,
                12, 13, 12, 13, 14, 15, 16, 17,
                16, 17, 18, 19, 20, 21, 20, 21,
                22, 23, 24, 25, 24, 25, 26, 27,
                28, 29, 28, 29, 30, 31, 32, 1
        };
        for (int i = 0; i < 48; i++) {
            byte index = (byte) (extendingPermutation[i] - 1); // subtract 1 to adjust for 0-based indexing
            byte byteIndex = (byte) (index / 8); // calculate the index of the byte that contains the bit
            byte bitIndex = (byte) (7 - (index % 8)); // calculate the index of the bit within the byte
            byte bit = (byte) ((block[byteIndex] >> bitIndex) & 0x01); // extract the bit at the specified position
            result[i / 8] |= (bit << (7 - (i % 8)));

        }
        return result;
    }

    public void feistelFunction(byte[] block, byte[] key) {
        block = extendedPermutation(block);
        BigInteger extebdedPermutationBlock = new BigInteger(block);
//        BigInteger secretKey = new BigInteger(key);
//        extebdedPermutationBlock = extebdedPermutationBlock.xor(secretKey);
        block = extebdedPermutationBlock.toByteArray();
        List<byte[]> sboxInput = new ArrayList<>();
        byte[] result = new byte[8];
        byte[] output = new byte[8];
        for (int i = 0; i < 8; i++) {
            int startIndex = i * 6 / 8;
            int shift = 2 * (i * 6 % 8);
            result[i] = (byte) ((block[startIndex] >> shift) & 0x3F);
            int row = ((result[i] & 0b100000) >> 4) | (result[i] & 0b000001);
            int col = (result[i] & 0b011110) >> 1;
            output[i] = (byte) S_BOXES[row][col*4+];
        }
        System.out.println(S_BOXES[1].length);
        test(result);
        test(output);


    }

    public void test(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            int unsignedByte = b & 0xFF;
            String binaryString = Integer.toBinaryString(unsignedByte);
            // Pad the binary string with leading zeros if necessary
            while (binaryString.length() < 6) {
                binaryString = "0" + binaryString;
            }
            sb.append(binaryString);
        }

        String binaryOutput = sb.toString();
        System.out.println(binaryOutput);
    }

    private byte[] bitRotateLeftByOne(byte[] table) {
        BigInteger shitfR = new BigInteger(table);
        BigInteger shitfL = new BigInteger(table);
        shitfR = shitfR.shiftRight(27);
        shitfL = shitfL.shiftLeft(1);
        byte[] result = shitfR.or(shitfL).toByteArray();
        return result;
    }

    private byte[] bitRotateLeftByTwo(byte[] table) {
        BigInteger shitfR = new BigInteger(table);
        BigInteger shitfL = new BigInteger(table);
        shitfR = shitfR.shiftRight(26);
        shitfL = shitfL.shiftLeft(2);
        byte[] result = shitfR.or(shitfL).toByteArray();
        return result;
    }

    public List<byte[]> permuttedKey(byte[] key, int roundNumber) {
        List<byte[]> permuttedKeyList = new ArrayList<>();

        byte[] permutedKey = PC1(key);

        BigInteger bigNum = new BigInteger(1, permutedKey);
        byte[] rightKey = bigNum.shiftRight(28).and(new BigInteger("FFFFFFF", 16)).toByteArray();
        byte[] leftKey = bigNum.and(new BigInteger("FFFFFFF", 16)).toByteArray();
        for (int i = 0; i < 16; i++) {
            if (roundNumber == 0 || roundNumber == 1 || roundNumber == 8 || roundNumber == 15) {
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

            permuttedKeyList.add(PC2(permutedKey));
        }
        return permuttedKeyList;
    }

    public void cipher(byte[] block, Key keys) {
        BigInteger BIGBlock = new BigInteger(block);
        BigInteger BIGKey = new BigInteger(keys.getKey(0));
        block = initialPermutation(BIGBlock.xor(BIGKey).toByteArray());
        BigInteger permuttedBlock = new BigInteger(block);
        byte[] rightBlock = permuttedBlock.shiftRight(32).and(new BigInteger("FFFFFFFF", 16)).toByteArray();
        byte[] leftBlock = permuttedBlock.and(new BigInteger("FFFFFFFF", 16)).toByteArray();

    }
}
