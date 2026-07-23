package com.example.demo.controller;

import com.example.demo.dto.BorrowResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.exception.UnauthorizedOperationException;
import com.example.demo.service.BorrowService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowController {

  @Autowired
  private BorrowService borrowService;

  @Autowired
  private UserService userService;

  // Borrow a book
  @PostMapping("/{userId}/{bookId}")
  public ResponseEntity<BorrowResponse> borrowBook(@PathVariable Long userId,
                                                   @PathVariable Long bookId) {
    validateUserAccess(userId, "You cannot borrow books for another user");
    BorrowResponse response = borrowService.borrowBook(userId, bookId);
    return ResponseEntity.ok(response);
  }

  // Return a book (Librarian only)
  @PostMapping("/return/{borrowId}")
  public ResponseEntity<BorrowResponse> returnBook(@PathVariable Long borrowId) {
    BorrowResponse response = borrowService.returnBook(borrowId);
    return ResponseEntity.ok(response);
  }

  // View own borrow history (authenticated user must match userId or be librarian)
  @GetMapping("/history/{userId}")
  public ResponseEntity<List<BorrowResponse>> getBorrowHistory(@PathVariable Long userId) {
    validateUserAccess(userId, "You cannot view history of another user");
    List<BorrowResponse> history = borrowService.getBorrowHistoryByUserId(userId);
    return ResponseEntity.ok(history);
  }

  // View all borrow records (Librarian only)
  @GetMapping("/records")
  public ResponseEntity<List<BorrowResponse>> getAllBorrowRecords() {
    List<BorrowResponse> records = borrowService.getAllBorrowRecords();
    return ResponseEntity.ok(records);
  }

  private void validateUserAccess(Long userId, String unauthorizedMsg) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new UnauthorizedOperationException("User is not authenticated");
    }
    String currentPrincipalEmail = authentication.getName();
    boolean isLibrarian = authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_LIBRARIAN"));

    if (!isLibrarian) {
      UserResponse targetUser = userService.getUserById(userId);
      if (targetUser == null || !targetUser.getEmail().equalsIgnoreCase(currentPrincipalEmail)) {
        throw new UnauthorizedOperationException(unauthorizedMsg);
      }
    }
  }
}