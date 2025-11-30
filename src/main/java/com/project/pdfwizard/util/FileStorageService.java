package com.project.pdfwizard.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


@Service
public class FileStorageService {

    private final Path pathDir;

    public FileStorageService(@Value("${pdf.location}") String path) {
        this.pathDir = Paths.get(path);
    }

    public String store(InputStream data, String path) throws IOException {
        Path target = pathDir.resolve(path);
        Files.createDirectories(target.getParent());
        try (OutputStream out = Files.newOutputStream(target, StandardOpenOption.CREATE_NEW)) {
            data.transferTo(out);

        }
        return target.toAbsolutePath().toString();
    }

    public InputStream retrieve(String storagePath) throws IOException {
        return Files.newInputStream(Paths.get(storagePath), StandardOpenOption.READ);
    }


    public boolean delete(String storagePath) throws IOException {
        return Files.deleteIfExists(Paths.get(storagePath));
    }


}
