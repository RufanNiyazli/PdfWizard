package com.project.pdfwizard;

import com.project.pdfwizard.entity.Document;
import com.project.pdfwizard.service.EmailService;
import com.project.pdfwizard.service.IPdfService;
import com.project.pdfwizard.util.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final IPdfService pdfService;
    private final EmailService emailService;
    private final FileStorageService fileStorageService;



    @PostMapping("/create")
    public ResponseEntity<ByteArrayResource> createPdf(@RequestBody Map<String, Object> payload) throws IOException {
        ByteArrayOutputStream baos = pdfService.createPDf(payload);
        byte[] pdfBytes = baos.toByteArray();

        String filename = (String) payload.getOrDefault("title", "document") + ".pdf";

        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(resource);
    }


    @PostMapping("/split-and-email")
    public ResponseEntity<Map<String, String>> splitAndEmail(
            @RequestParam("file") MultipartFile file,
            @RequestParam("perPage") int perPage,
            @RequestParam("email") String email) {

        try (InputStream inputStream = file.getInputStream()) {
            List<ByteArrayOutputStream> splitPdfs = pdfService.split(inputStream, perPage);

            if (!splitPdfs.isEmpty()) {
                ByteArrayOutputStream firstPart = splitPdfs.get(0);
                byte[] attachmentBytes = firstPart.toByteArray();
                String filename = file.getOriginalFilename().replace(".pdf", "") + "_part1.pdf";
                String subject = "Your Split PDF - Part 1";
                String text = "Please find the first part of your split PDF attached.";

                emailService.sendWithAttachment(email, subject, text, filename, attachmentBytes);
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", String.format("PDF split into %d parts and the first part has been emailed to %s.", splitPdfs.size(), email));
            return ResponseEntity.accepted().body(response);

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to process PDF: " + e.getMessage()));
        }
    }

}