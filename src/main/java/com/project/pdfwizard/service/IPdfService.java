package com.project.pdfwizard.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public interface IPdfService {

    public ByteArrayOutputStream createPDf(Map<String ,Object> payload) throws IOException;
}
