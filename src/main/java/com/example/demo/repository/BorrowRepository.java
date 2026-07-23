package com.example.demo.repository;

import com.example.demo.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface BorrowRepository extends JpaRepository<BorrowRecord, Long> {

  @Query("SELECT r FROM BorrowRecord r JOIN FETCH r.user JOIN FETCH r.book WHERE r.user.id = :userId")
  List<BorrowRecord> findByUserId(@Param("userId") Long userId);

  Optional<BorrowRecord> findByBookIdAndReturnDateIsNull(Long bookId);

  @Query("SELECT r FROM BorrowRecord r JOIN FETCH r.user JOIN FETCH r.book WHERE r.user.id = :userId AND r.borrowDate BETWEEN :startDate AND :endDate")
  List<BorrowRecord> findByUserIdAndBorrowDateBetween(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

  @Query("SELECT r FROM BorrowRecord r JOIN FETCH r.user JOIN FETCH r.book")
  List<BorrowRecord> findAllWithUserAndBook();
}