package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatabaseSeeder implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private BorrowRepository borrowRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    // 1. Seed Student if not present
    User student = userRepository.findByEmail("student@library.com").orElse(null);
    if (student == null) {
      student = new User();
      student.setName("John Doe");
      student.setEmail("student@library.com");
      student.setPassword(passwordEncoder.encode("password123"));
      student.setRole(Role.STUDENT);
      student = userRepository.save(student);
    }

    // 2. Seed Librarian if not present
    User librarian = userRepository.findByEmail("librarian@library.com").orElse(null);
    if (librarian == null) {
      librarian = new User();
      librarian.setName("Jane Librarian");
      librarian.setEmail("librarian@library.com");
      librarian.setPassword(passwordEncoder.encode("password123"));
      librarian.setRole(Role.LIBRARIAN);
      userRepository.save(librarian);
    }

    // 3. Seed Books if not present
    Book book1 = bookRepository.findAll().stream()
        .filter(b -> b.getIsbn().equals("9780743273565"))
        .findFirst().orElse(null);
    if (book1 == null) {
      book1 = new Book();
      book1.setTitle("The Great Gatsby");
      book1.setAuthor("F. Scott Fitzgerald");
      book1.setIsbn("9780743273565");
      book1.setAvailable(true);
      book1 = bookRepository.save(book1);
    }

    Book book2 = bookRepository.findAll().stream()
        .filter(b -> b.getIsbn().equals("9780061120084"))
        .findFirst().orElse(null);
    if (book2 == null) {
      book2 = new Book();
      book2.setTitle("To Kill a Mockingbird");
      book2.setAuthor("Harper Lee");
      book2.setIsbn("9780061120084");
      book2.setAvailable(false); // Borrowed (overdue simulation)
      book2 = bookRepository.save(book2);
    }

    Book book3 = bookRepository.findAll().stream()
        .filter(b -> b.getIsbn().equals("9780451524935"))
        .findFirst().orElse(null);
    if (book3 == null) {
      book3 = new Book();
      book3.setTitle("1984");
      book3.setAuthor("George Orwell");
      book3.setIsbn("9780451524935");
      book3.setAvailable(true); // Returned late simulation
      book3 = bookRepository.save(book3);
    }

    // 4. Seed Borrow Records if student has no records
    if (borrowRepository.findByUserId(student.getId()).isEmpty()) {
      // Overdue Borrow Record (Active, 6 days past due)
      BorrowRecord rec1 = new BorrowRecord();
      rec1.setUser(student);
      rec1.setBook(book2);
      rec1.setBorrowDate(LocalDate.now().minusDays(20));
      rec1.setDueDate(LocalDate.now().minusDays(20).plusDays(14)); // Due 6 days ago
      rec1.setReturnDate(null);
      rec1.setFine(0.0);
      borrowRepository.save(rec1);

      // Returned Late Borrow Record (6 days late, fine stored in DB)
      BorrowRecord rec2 = new BorrowRecord();
      rec2.setUser(student);
      rec2.setBook(book3);
      rec2.setBorrowDate(LocalDate.now().minusDays(30));
      rec2.setDueDate(LocalDate.now().minusDays(30).plusDays(14)); // Due 16 days ago
      rec2.setReturnDate(LocalDate.now().minusDays(10)); // Returned 10 days ago (6 days late)
      rec2.setFine(60.0);
      borrowRepository.save(rec2);
    }

    System.out.println("====== Dummy Data Seeded/Verified Successfully ======");
    System.out.println("Student Credentials: student@library.com / password123");
    System.out.println("Librarian Credentials: librarian@library.com / password123");
    System.out.println("============================================");
  }
}
