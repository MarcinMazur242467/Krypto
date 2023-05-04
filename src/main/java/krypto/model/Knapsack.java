package krypto.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Knapsack implements Serializable {


    private List<BigInteger> privateKey = new ArrayList<>();
    private final List<BigInteger> publicKey = new ArrayList<>();
    private BigInteger M;
    private BigInteger N;

    public void clearKnapsack() {
        privateKey.clear();
        publicKey.clear();
        M = BigInteger.valueOf(0);
        N = BigInteger.valueOf(0);
    }

    public List<BigInteger> getPrivateKey() {
        return privateKey;
    }

    public List<BigInteger> getPublicKey() {
        return publicKey;
    }

    public BigInteger getM() {
        return M;
    }

    public BigInteger getN() {
        return N;
    }

    private Random random = new Random();

    private String intToHex(int value) {
        StringBuilder builder = new StringBuilder();
        String hexDigits = "0123456789ABCDEF";

        // Initialize an empty string to hold the result
        String hexadecimal = "";

        // Convert the decimal number to hexadecimal
        while (value > 0) {
            int remainder = value % 16;
            hexadecimal = hexDigits.charAt(remainder) + hexadecimal;
            value = value / 16;
        }
        return hexadecimal;
    }

    public void generatePrivateKey() {
        clearKnapsack();
        // DLUGOSC KLUCZA 2 ^ numberBits -1
        BigInteger first = new BigInteger(15, random);
        privateKey.add(first);
        while (privateKey.size() != 8) {
            int n = random.nextInt(first.intValue());
            BigInteger next = first.add(new BigInteger(Integer.toString(n))).add(BigInteger.valueOf(1));
            privateKey.add(next);
            first = first.add(next);
        }
        generateMnN();
        generatePublicKey();
    }

    public void printKnapsack() {
        System.out.println();
        System.out.println("Private=" + getPrivateKey());
        System.out.println("Public=" + getPublicKey());
        System.out.println("M=" + getM() + " In hexa: " + intToHex(getM().intValue()));
        System.out.println("N=" + getN() + " In hexa: " + intToHex(getN().intValue()));
        System.out.println();
    }

    public void loadPrivateKey(List<BigInteger> PrivateKey) {
        clearKnapsack();
        this.privateKey = PrivateKey;
        generateMnN();
        generatePublicKey();
    }

    private void generateMnN() {
        BigInteger sum = new BigInteger("0");
        for (BigInteger i : privateKey) {
            sum = sum.add(i);
        }
        sum = sum.add(sum).add(BigInteger.valueOf(1));
        M = sum;


        //TO TRZEBA BEDZIE NAPRAWIC TAK NIE MOZE BYC
        int potentialN = sum.intValue() - 10000;
        BigInteger potentialNBigInt = new BigInteger(Integer.toString(potentialN));

        while (true) {
            int gcd = M.gcd(potentialNBigInt).intValue();
            if (gcd == 1) {
                N = potentialNBigInt;
                break;
            }
            potentialNBigInt = potentialNBigInt.subtract(BigInteger.valueOf(-1));
        }

    }

    public BigInteger encrypt(char data) {
        BigInteger sum = BigInteger.valueOf(0);
        for (int i = 7; i >= 0; i--) {
            int bitValue = (data >> i) & 1;
            if (bitValue == 1) {
                sum = sum.add(publicKey.get(7 - i));
            }
        }
        return sum;
    }
    private char reverseBits(char b) {
        char result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 1;
            result |= (b & 1);
            b >>= 1;
        }
        return result;
    }

    public char decrypt(BigInteger input) {
        char c = 0;
        BigInteger invers = N.modInverse(M);
        input = input.multiply(invers).mod(M);

        for (int i = 0; i < 7; i++) {
            int compare = privateKey.get(7-i).compareTo(input);
            if (compare <= 0) {
                c = (char) ((c|1)<<1);
                input = input.subtract(privateKey.get(7-i));
            } else {
                 c = (char) (c << 1);
            }
        }
        return reverseBits(c);
    }
    public static void printByteBits(char b) {
        for (int i = 7; i >= 0; i--) {
            System.out.print((b >> i) & 1);
        }
        System.out.println();
    }

    private void generatePublicKey() {
        for (int i = 0; i < 8; i++) {
            publicKey.add(privateKey.get(i).multiply(N).mod(M));
        }
    }
}
