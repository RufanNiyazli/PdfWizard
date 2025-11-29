package com.project.pdfwizard.service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements IPdfService {

    @Override
    public ByteArrayOutputStream createPDf(Map<String, Object> payload) throws IOException {
        PDDocument document = new PDDocument();
        PDPage my_page = new PDPage(PDRectangle.LETTER);
        document.addPage(my_page);


        PDPageContentStream contentStream = new PDPageContentStream(document, my_page);
        PDFont font = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
        float fontSize = 12f;
        float lineSpacing = 1.5f * fontSize;
        float margin = 50;
        PDRectangle mediaBox = my_page.getMediaBox();
        float width = mediaBox.getWidth() - 2 * margin;
        float startY = mediaBox.getHeight() - margin;
        float currentY = mediaBox.getHeight() - margin;

        contentStream.beginText();
        contentStream.newLineAtOffset(margin, startY);
        contentStream.setFont(font, fontSize);
        String title = (String) payload.getOrDefault("title", "Document");
        contentStream.showText(title);
        currentY -= lineSpacing;


        contentStream.newLineAtOffset(0, -lineSpacing * 2);
        contentStream.setFont(font, fontSize);

        List<String> paragraphs = (List<String>) payload.getOrDefault("paragraphs", Collections.emptyList());

        for (String paragraph : paragraphs) {
            List<String> lines = wrapText(paragraph, width, fontSize, font);
            for (String line : lines) {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -lineSpacing);
                currentY -= lineSpacing;
                if (currentY < margin) {
                    contentStream.endText();
                    contentStream.close();
                    my_page = new PDPage(PDRectangle.LETTER);
                    document.addPage(my_page);
                    contentStream = new PDPageContentStream(document, my_page);
                    contentStream.beginText();
                    contentStream.setFont(font, fontSize);
                    currentY = mediaBox.getHeight() - margin;
                    contentStream.newLineAtOffset(margin, currentY);
                }
            }
            contentStream.newLineAtOffset(0, -lineSpacing);//bos setir qoyuruq
        }


        contentStream.endText();
        contentStream.close();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();
        return baos;
    }

    private List<String> wrapText(String paragraph, float width, float fontSize, PDFont font) throws IOException {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        String[] words = paragraph.split("\\s+");
        for (String word : words) {
            String candidate = line.length() == 0 ? word : line + " " + word;
            float wWidth = font.getStringWidth(candidate) / 1000 * fontSize;
            if (wWidth > width) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                if (!line.isEmpty()) line.append(" ");
                line.append(word);
            }


        }
        if (!line.isEmpty()) lines.add(line.toString());
        return lines;


    }
}
