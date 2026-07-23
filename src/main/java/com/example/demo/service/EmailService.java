package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmailService {

  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

  @Autowired
  private JavaMailSender mailSender;

  @Async
  public void sendBorrowReceiptEmail(String toEmail, String studentName, String bookTitle, LocalDate borrowDate,
      LocalDate dueDate) {
    if (mailSender == null) {
      logger.warn("JavaMailSender is not initialized. Skipping email delivery.");
      return;
    }

    String mailContent = "Dear " + studentName + ",\n\n" +
        "Thank you for using the Library Portal. This email serves as a receipt for borrowing the book: \"" + bookTitle
        + "\".\n\n" +
        "Borrow Date: " + borrowDate + "\n" +
        "Expected Due Date: " + dueDate + "\n\n" +
        "Please return the book on or before the due date to avoid late fees of INR 10.00 per day.\n\n" +
        "Best regards,\n" +
        "Library Management Team";

    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom("snigdhanayak.0502@gmail.com");
      message.setTo(toEmail);
      message.setSubject("Library Borrow Receipt: " + bookTitle);
      message.setText(mailContent);

      mailSender.send(message);
      logger.info("Email receipt sent successfully to {}", toEmail);
    } catch (Exception e) {
      logger.warn("SMTP Mail Server is not reachable. Falling back to Console Receipt Log:\n" +
          "--------------------------------------------------\n" +
          "TO: {}\n" +
          "SUBJECT: Library Borrow Receipt: {}\n" +
          "CONTENT:\n{}\n" +
          "--------------------------------------------------",
          toEmail, bookTitle, mailContent);
    }
  }
}
