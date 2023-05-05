package krypto.model;

import java.io.*;
import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KnapsackFileManager {

    private final File fileName;

    public KnapsackFileManager(File fileName) {
        this.fileName = fileName;
    }

    public byte[] convertBigIntegersToByteArray(List<BigInteger> bigIntegerList) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bigIntegerList.size() * 8);
        for (BigInteger bigInteger : bigIntegerList) {
            byteBuffer.putLong(bigInteger.longValue());
        }
        return byteBuffer.array();
    }

    public void saveBigIntegersToFile(List<BigInteger> bigIntegerList) throws IOException {
        char[] charArray = new char[bigIntegerList.size()];
        for (int i = 0; i < bigIntegerList.size(); i++) {
            charArray[i] = (char) bigIntegerList.get(i).byteValue();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(charArray);
        writer.close();
    }

    public List<BigInteger> readBigIntegersFromFile() throws IOException {
        List<BigInteger> bigIntegerList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        int ch;
        while ((ch = reader.read()) != -1) {
            bigIntegerList.add(BigInteger.valueOf(ch));
        }
        reader.close();
        return bigIntegerList;
    }

}
