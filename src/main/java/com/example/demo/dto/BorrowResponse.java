package com.example.demo.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowResponse {
  private Long id;
  private Long userId;
  private String userName;
  private Long bookId;
  private String bookTitle;
  private LocalDate borrowDate;
  private LocalDate dueDate;
  private LocalDate returnDate;
  private Double fine;
}
