package krypto.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.codec.binary.Hex;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import com.itextpdf.text.*;
import org.apache.commons.codec.*;


public class FileManager {
    private final File fileName;
    private byte[] fileinbytes;

    public FileManager(File fileName) {
        this.fileName = fileName;
    }


    public byte[] getFileinbytes() {
        return fileinbytes;
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

//        File file = new File(fileName.getPath());
//        byte[] bytes = new byte[(int) file.length()];
//
//        try (FileInputStream fis = new FileInputStream(file)) {
//            fis.read(bytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        fileinbytes = bytes;
//
//        return fileinbytes;
    }


    public void write(String s) throws IOException, DocumentException {
        if(fileName.toString().contains(".pdf")){
            Document document = new Document();
            PdfWriter.getInstance(document,new FileOutputStream(fileName));
            Paragraph paragraph = new Paragraph();
            document.open();
            paragraph.add(s);
            document.add(paragraph);
            document.close();
        }
        else {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(s);
            writer.close();
        }
//        File newFile = new File(fileName.getPath());
//        newFile.createNewFile();
//        try (FileOutputStream fos = new FileOutputStream(newFile)) {
//            fos.write(fileinbytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}

