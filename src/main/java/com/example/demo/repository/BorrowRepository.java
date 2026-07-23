package com.example.demo.repository;

import com.example.demo.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface BorrowRepository extends JpaRepository<BorrowRecord, Long> {

  List<BorrowRecord> findByUserId(Long userId);

  Optional<BorrowRecord> findByBookIdAndReturnDateIsNull(Long bookId);

  List<BorrowRecord> findByUserIdAndBorrowDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}