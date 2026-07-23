package com.example.demo.service;

import com.example.demo.dto.BorrowResponse;
import com.example.demo.entity.*;
import com.example.demo.exception.*;
import com.example.demo.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowService {

  private static final Logger logger = LoggerFactory.getLogger(BorrowService.class);

  @Autowired
  private BorrowRepository borrowRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BookRepository bookRepository;

  public BorrowResponse borrowBook(Long userId, Long bookId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));

    if (!book.isAvailable()) {
      throw new IllegalArgumentException("Book is already borrowed");
    }

    book.setAvailable(false);
    bookRepository.save(book);

    BorrowRecord record = new BorrowRecord();
    record.setUser(user);
    record.setBook(book);
    record.setBorrowDate(LocalDate.now());
    record.setDueDate(LocalDate.now().plusDays(14));
    record.setFine(0.0);

    BorrowRecord savedRecord = borrowRepository.save(record);
    logger.info("Book Borrowed: RecordID={}, UserEmail={}, BookTitle={}", savedRecord.getId(), user.getEmail(), book.getTitle());

    return convertToResponse(savedRecord);
  }

  public BorrowResponse returnBook(Long borrowId, String currentEmail, boolean isLibrarian) {
    BorrowRecord record = borrowRepository.findById(borrowId)
        .orElseThrow(() -> new BorrowRecordNotFoundException("Borrow record not found with ID: " + borrowId));

    if (!isLibrarian && !record.getUser().getEmail().equalsIgnoreCase(currentEmail)) {
      throw new UnauthorizedOperationException("You cannot return a book borrowed by another user");
    }

    if (record.getReturnDate() != null) {
      throw new IllegalArgumentException("Book is already returned");
    }

    record.setReturnDate(LocalDate.now());

    long lateDays = ChronoUnit.DAYS.between(
        record.getDueDate(),
        record.getReturnDate());

    if (lateDays > 0) {
      record.setFine((double) lateDays * 10.0); // ₹10 per day
    } else {
      record.setFine(0.0);
    }

    Book book = record.getBook();
    book.setAvailable(true);
    bookRepository.save(book);

    BorrowRecord savedRecord = borrowRepository.save(record);
    logger.info("Book Returned: RecordID={}, UserEmail={}, BookTitle={}, Fine={}", savedRecord.getId(), record.getUser().getEmail(), book.getTitle(), savedRecord.getFine());

    return convertToResponse(savedRecord);
  }

  public List<BorrowResponse> getBorrowHistoryByUserId(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException("User not found with ID: " + userId);
    }
    return borrowRepository.findByUserId(userId).stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
  }

  public List<BorrowResponse> getAllBorrowRecords() {
    return borrowRepository.findAll().stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
  }

  private BorrowResponse convertToResponse(BorrowRecord record) {
    return new BorrowResponse(
        record.getId(),
        record.getUser().getId(),
        record.getUser().getName(),
        record.getBook().getId(),
        record.getBook().getTitle(),
        record.getBorrowDate(),
        record.getDueDate(),
        record.getReturnDate(),
        record.getFine()
    );
  }
}