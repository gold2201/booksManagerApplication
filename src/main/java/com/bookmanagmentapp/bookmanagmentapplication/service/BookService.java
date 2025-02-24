package com.bookmanagmentapp.bookmanagmentapplication.service;

import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    /* public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthors_Name(author);
    }*/

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга не найдена"));
    }

    // Новый метод для получения книг по имени автора
    public List<Book> getBooksByAuthorName(String authorName) {
        List<Book> books = bookRepository.findByAuthorName(authorName);
        if (books.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книги по данному автору не найдены");
        }
        return books;
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}

