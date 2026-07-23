package com.example.demo.controller;

import com.example.demo.dto.BookRequest;
import com.example.demo.dto.BookResponse;
import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

  @Autowired
  private BookService bookService;

  // Add Book (Librarian only)
  @PostMapping
  public ResponseEntity<BookResponse> addBook(@Valid @RequestBody BookRequest request) {
    Book book = convertToEntity(request);
    Book savedBook = bookService.addBook(book);
    return new ResponseEntity<>(convertToResponse(savedBook), HttpStatus.CREATED);
  }

  // Get All Books (Authenticated users)
  @GetMapping
  public ResponseEntity<List<BookResponse>> getAllBooks(@RequestParam(required = false) String search) {
    List<Book> booksList;
    if (search != null && !search.trim().isEmpty()) {
      booksList = bookService.searchBooks(search);
    } else {
      booksList = bookService.getAllBooks();
    }
    List<BookResponse> books = booksList.stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
    return ResponseEntity.ok(books);
  }

  // Get Available Books (Authenticated users)
  @GetMapping("/available")
  public ResponseEntity<List<BookResponse>> getAvailableBooks() {
    List<BookResponse> books = bookService.getAvailableBooks().stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
    return ResponseEntity.ok(books);
  }

  // Update Book (Librarian only)
  @PutMapping("/{id}")
  public ResponseEntity<BookResponse> updateBook(@PathVariable Long id,
                                                 @Valid @RequestBody BookRequest request) {
    Book book = convertToEntity(request);
    Book updatedBook = bookService.updateBook(id, book);
    return ResponseEntity.ok(convertToResponse(updatedBook));
  }

  // Delete Book (Librarian only)
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteBook(@PathVariable Long id) {
    bookService.deleteBook(id);
    return ResponseEntity.ok("Book deleted successfully!");
  }

  private BookResponse convertToResponse(Book book) {
    return new BookResponse(
        book.getId(),
        book.getTitle(),
        book.getAuthor(),
        book.getIsbn(),
        book.isAvailable()
    );
  }

  private Book convertToEntity(BookRequest request) {
    Book book = new Book();
    book.setTitle(request.getTitle());
    book.setAuthor(request.getAuthor());
    book.setIsbn(request.getIsbn());
    return book;
  }
}