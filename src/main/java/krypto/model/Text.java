package krypto.model;

import java.nio.ByteBuffer;
import java.util.Base64;

public class Text {

    public static byte[][] divideIntoBlocks(String text){
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
    public static String blocksToString(byte[] block){
        return Base64.getEncoder().encodeToString(block);
    }
}
