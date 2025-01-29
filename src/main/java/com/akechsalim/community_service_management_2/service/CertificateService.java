package com.akechsalim.community_service_management_2.service;

import com.akechsalim.community_service_management_2.model.TrainingModule;
import com.akechsalim.community_service_management_2.model.User;
import com.akechsalim.community_service_management_2.repository.TrainingModuleRepository;
import com.akechsalim.community_service_management_2.repository.UserRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class CertificateService {

    private final UserRepository userRepository;

    private final TrainingModuleRepository trainingModuleRepository;

    public CertificateService(UserRepository userRepository, TrainingModuleRepository trainingModuleRepository) {
        this.userRepository = userRepository;
        this.trainingModuleRepository = trainingModuleRepository;
    }

    public byte[] generateCertificate(Long volunteerId, Long moduleId) {
        User volunteer = userRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        TrainingModule module = trainingModuleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Training module not found"));

        // Generate PDF using Apache PDFBox
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Certificate of Completion");
            contentStream.endText();

            contentStream.setFont(PDType1Font.HELVETICA, 18);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 650);
            contentStream.showText("This certifies that " + volunteer.getUsername() + " has completed the training module:");
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(100, 600);
            contentStream.showText(module.getTitle());
            contentStream.endText();

            contentStream.close();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate certificate", e);
        }
    }
}
