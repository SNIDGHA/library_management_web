package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

  @Autowired
  private BookService bookService;

  // Add Book
  @PostMapping
  public Book addBook(@RequestBody Book book) {
    return bookService.addBook(book);
  }

  // Get All Books
  @GetMapping
  public List<Book> getAllBooks() {
    return bookService.getAllBooks();
  }

  // Get Available Books
  @GetMapping("/available")
  public List<Book> getAvailableBooks() {
    return bookService.getAvailableBooks();
  }

  // Update Book
  @PutMapping("/{id}")
  public Book updateBook(@PathVariable Long id,
      @RequestBody Book updatedBook) {

    return bookService.updateBook(id, updatedBook);
  }

  // Delete Book
  @DeleteMapping("/{id}")
  public String deleteBook(@PathVariable Long id) {

    bookService.deleteBook(id);

    return "Book deleted successfully!";
  }
}