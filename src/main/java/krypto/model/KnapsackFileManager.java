package krypto.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
        byte[] byteArray = convertBigIntegersToByteArray(bigIntegerList);

        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName.getPath()));
        fileOutputStream.write(byteArray);
        fileOutputStream.close();
    }

    public List<BigInteger> readBigIntegersFromFile() throws IOException {
        File file = new File(fileName.getPath());
        byte[] byteArray = new byte[(int) file.length()];

        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(byteArray);
        fileInputStream.close();

        List<BigInteger> bigIntegerList = new ArrayList<>();
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        while (byteBuffer.hasRemaining()) {
            try{
                bigIntegerList.add(BigInteger.valueOf(byteBuffer.getLong()));
            }
            catch (BufferUnderflowException e){
                byte[] remainingBytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(remainingBytes);
                byte[] extendedBytes = new byte[8];
                System.arraycopy(remainingBytes, 0, extendedBytes, 0, remainingBytes.length);
                for (int i = remainingBytes.length; i < extendedBytes.length; i++) {
                    extendedBytes[i] = 0;
                }
                BigInteger remainingValue = new BigInteger(extendedBytes);
                bigIntegerList.add(remainingValue);
            }
        }

        return bigIntegerList;
    }

}
