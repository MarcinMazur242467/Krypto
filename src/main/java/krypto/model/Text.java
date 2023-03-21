package krypto.model;

import java.nio.ByteBuffer;

public class Text {

    public static byte[][] divideStringIntoBlocks(String text) {
        int counter = 0;
        byte[] bytes = text.getBytes();
        int blockCount = (int) Math.ceil((double) bytes.length / 8);
        byte[][] blocks = new byte[blockCount+1][8];
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
    public static byte[][] divideFileIntoBlocks(byte[] tab){
        int counter = 0;
        int blockCount = (int) Math.ceil((double) tab.length / 8);
        byte[][] blocks = new byte[blockCount+1][8];
        int index = 0;
        for (int i = 0; i < blockCount; i++) {
            for (int j = 0; j < 8; j++) {
                if (index < tab.length) {
                    blocks[i][j] = tab[index];
                    index++;
                } else {
                    blocks[i][j] = 0;
                }
            }
        }
        return blocks;
    }
}
