package com.demo.landmark.serviceimpl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PDFParserService extends PDFTextStripper {

    static List<String> lines;

    public PDFParserService() throws IOException {
        // Class constructor
    }


    public boolean readPDFFile(String filePath) {

        try (Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream())) {
            lines = new ArrayList<>();
            PDDocument document = PDDocument.load( new File(filePath) );
            PDFTextStripper stripper = new PDFParserService();
            stripper.setSortByPosition(true);
            stripper.setStartPage(0);
            stripper.setEndPage( document.getNumberOfPages() );

            stripper.writeText(document, dummy);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeDocument(document);
        }


        return true;
    }

    private void closeDocument(PDDocument document) {
        if (document != null) {
            try {
                document.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }

    @Override
    protected void writeString(String str, List<TextPosition> textPositions) {
        lines.add(str);

    }

}