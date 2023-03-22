package krypto.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;


public class FileManager {
    private final File fileName;

    public FileManager(File fileName) {
        this.fileName = fileName;
    }


    public byte[] read() throws IOException {
        byte[] buffer;
        if(fileName.toString().contains(".pdf")){
            PDDocument document = PDDocument.load(new File(fileName.getPath()));
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            buffer = text.getBytes();
            document.close();
        }else {
            File file = new File(fileName.getPath());
            int fileSize = (int) file.length();
            buffer = new byte[fileSize];
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(buffer);
            inputStream.close();

        }
        return buffer;
    }


    public void write(String s) throws IOException {
        if(fileName.toString().contains(".pdf")){
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            float fontSize = 12;
            final int MAX_LENGTH = 50;
            List<String> result = new ArrayList<>();
            s = s.replaceAll("\n", " ");
            String[] words = s.split("\\s+");
            String current = "";
            for (String word : words) {
                if ((current + " " + word).length() <= MAX_LENGTH) {
                    current += " " + word;
                } else {
                    result.add(current.trim());
                    current = word;
                }
            }
            if (!current.isEmpty()) {
                result.add(current.trim());
            }
            contentStream.setFont(PDType1Font.TIMES_ROMAN,fontSize);
            contentStream.moveTextPositionByAmount(50, 700);
            for (String value : result) {
                contentStream.showText(value);
                contentStream.newLine();
            }
            contentStream.endText();
            contentStream.close();
            document.save(fileName);
            document.close();
        }
        else {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(s);
            writer.close();
        }
    }
}


