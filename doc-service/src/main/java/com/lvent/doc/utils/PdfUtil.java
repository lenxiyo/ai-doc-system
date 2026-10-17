package com.lvent.doc.utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PdfUtil {

    public static void extractText(File file) throws IOException {
        PDDocument pdDocument = Loader.loadPDF(file);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(pdDocument);
        System.out.println(text);
        pdDocument.close();
    }

    public static void textSplitter(File file) throws IOException {
        PDDocument pdDocument = Loader.loadPDF(file);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(pdDocument);

        for (int i = 0; i < text.length();) {
            int j = i + 500;
            if (j > text.length()){
                j = text.length();
            }
            String substring = text.substring(i, j);
            System.out.println(substring);
            i = j;
        }
        pdDocument.close();
    }

}
