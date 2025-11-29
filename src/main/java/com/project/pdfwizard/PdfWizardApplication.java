package com.project.pdfwizard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PdfWizardApplication {

    public static void main(String[] args) {
        SpringApplication.run(PdfWizardApplication.class, args);
    }

}
