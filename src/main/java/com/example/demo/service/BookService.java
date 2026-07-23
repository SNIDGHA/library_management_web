package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

  private static final Logger logger = LoggerFactory.getLogger(BookService.class);

  @Autowired
  private BookRepository bookRepository;

  // Add Book
  public Book addBook(Book book) {
    Book savedBook = bookRepository.save(book);
    logger.info("Book Added: ID={}, Title={}, ISBN={}", savedBook.getId(), savedBook.getTitle(), savedBook.getIsbn());
    return savedBook;
  }

  // Get All Books
  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  // Get Available Books
  public List<Book> getAvailableBooks() {
    return bookRepository.findByAvailable(true);
  }

  // Update Book
  public Book updateBook(Long id, Book updatedBook) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));

    book.setTitle(updatedBook.getTitle());
    book.setAuthor(updatedBook.getAuthor());
    book.setIsbn(updatedBook.getIsbn());
    book.setAvailable(updatedBook.isAvailable());

    Book savedBook = bookRepository.save(book);
    logger.info("Book Updated: ID={}, Title={}", savedBook.getId(), savedBook.getTitle());
    return savedBook;
  }

  // Delete Book
  public void deleteBook(Long id) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));

    bookRepository.delete(book);
    logger.info("Book Deleted: ID={}, Title={}", id, book.getTitle());
  }
}