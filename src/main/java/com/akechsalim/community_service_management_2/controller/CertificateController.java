package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.service.CertificateService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateCertificate(@RequestParam Long volunteerId, @RequestParam Long moduleId) {
        byte[] certificate = certificateService.generateCertificate(volunteerId, moduleId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate.pdf")
                .body(certificate);
    }
}
