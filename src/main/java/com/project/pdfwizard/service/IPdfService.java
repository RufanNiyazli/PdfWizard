package com.project.pdfwizard.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IPdfService {

    public ByteArrayOutputStream createPDf(Map<String ,Object> payload) throws IOException;
    public List<ByteArrayOutputStream> split(InputStream pdfStream, int perPage) throws IOException;
}
