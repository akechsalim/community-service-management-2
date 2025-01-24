package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    private final JavaMailSender emailSender;

    public ContactController(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostMapping("/contact")
    public ResponseEntity<String> submitContact(@RequestBody Contact contact) {
        try {
            // Log the received contact
            logger.info("Received contact: {}", contact);

            // Send email notifications directly without saving to database
            sendUserAcknowledgementEmail(contact);
            sendAdminNotificationEmail(contact);

            return ResponseEntity.ok("Thanks for contacting us! Your message has been received.");
        } catch (Exception e) {
            logger.error("Error processing contact form submission", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request.");
        }
    }

    private void sendUserAcknowledgementEmail(Contact contact) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(contact.getEmail()); // Send to the contact's email, which should be accessible on their phone
        message.setSubject("Thank you for contacting us!");
        message.setText("Your message has been received. We'll get back to you soon.");
        emailSender.send(message);
    }

    private void sendAdminNotificationEmail(Contact contact) {
        SimpleMailMessage message = new SimpleMailMessage();
        // Assuming you want this to go to an admin's email which they check on their phone
        message.setTo("2020user4476@gmail.com"); // Replace with your admin's email
        message.setSubject("New Contact Submission");
        message.setText("New contact received:\n" +
                "Name: " + contact.getName() + "\n" +
                "Email: " + contact.getEmail() + "\n" +
                "Message: " + contact.getMessage());
        emailSender.send(message);
    }
}

