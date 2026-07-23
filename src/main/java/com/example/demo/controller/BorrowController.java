package com.example.demo.controller;

import com.example.demo.entity.BorrowRecord;
import com.example.demo.service.BorrowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrow")
public class BorrowController {

  @Autowired
  private BorrowService borrowService;

  @PostMapping("/{userId}/{bookId}")
  public BorrowRecord borrowBook(@PathVariable Long userId,
      @PathVariable Long bookId) {

    return borrowService.borrowBook(userId, bookId);
  }

  @PostMapping("/return/{borrowId}")
  public BorrowRecord returnBook(@PathVariable Long borrowId) {
    return borrowService.returnBook(borrowId);
  }
}