package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

  @Autowired
  private BookRepository bookRepository;

  // Add Book
  public Book addBook(Book book) {
    return bookRepository.save(book);
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
        .orElseThrow(() -> new RuntimeException("Book not found"));

    book.setTitle(updatedBook.getTitle());
    book.setAuthor(updatedBook.getAuthor());
    book.setIsbn(updatedBook.getIsbn());
    book.setAvailable(updatedBook.isAvailable());

    return bookRepository.save(book);
  }

  // Delete Book
  public void deleteBook(Long id) {

    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Book not found"));

    bookRepository.delete(book);
  }
}