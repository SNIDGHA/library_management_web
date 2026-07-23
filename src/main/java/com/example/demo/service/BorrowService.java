package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class BorrowService {

  @Autowired
  private BorrowRepository borrowRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BookRepository bookRepository;

  public BorrowRecord borrowBook(Long userId, Long bookId) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("Book not found"));

    if (!book.isAvailable()) {
      throw new RuntimeException("Book already borrowed");
    }

    book.setAvailable(false);
    bookRepository.save(book);

    BorrowRecord record = new BorrowRecord();
    record.setUser(user);
    record.setBook(book);
    record.setBorrowDate(LocalDate.now());
    record.setDueDate(LocalDate.now().plusDays(14));
    record.setFine(0.0);

    return borrowRepository.save(record);
  }

  public BorrowRecord returnBook(Long borrowId) {

    BorrowRecord record = borrowRepository.findById(borrowId)
        .orElseThrow(() -> new RuntimeException("Borrow record not found"));

    record.setReturnDate(LocalDate.now());

    long lateDays = ChronoUnit.DAYS.between(
        record.getDueDate(),
        record.getReturnDate());

    if (lateDays > 0) {
      record.setFine((double) lateDays * 10); // ₹10 per day
    }

    Book book = record.getBook();
    book.setAvailable(true);

    bookRepository.save(book);

    return borrowRepository.save(record);
  }

}