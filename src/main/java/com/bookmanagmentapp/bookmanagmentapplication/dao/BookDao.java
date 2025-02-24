/* package com.bookmanagmentapp.bookmanagmentapplication.dao;

import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public class BookDao {
    private final List<Book> books = List.of(// пока что имитируем бд
            Book.builder().id(1).title("Война и мир").author("Лев Толстой").build(),
            Book.builder().id(2).title("Анна Каренина").author("Лев Толстой").build(),
            Book.builder().id(3).title("Преступление и наказание").author("Фёдор Достоевский").build()
    );

    public List<Book> findByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .toList();
    }

    public Book findById(int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга не найдена"));
    }
}

Model
package com.bookmanagmentapp.bookmanagmentapplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Book { // модель книги
    private int id;
    private String title;
    private String author;
}

Controller
package com.bookmanagmentapp.bookmanagmentapplication.controller;

import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import com.bookmanagmentapp.bookmanagmentapplication.service.BookService;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books management")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/search") // Получаем параметр из запроса
    public List<Book> getBooksByAuthor(@RequestParam String author) {
        return bookService.getBooksByAuthor(author);
    }

    @GetMapping("/{id}") // Получаем параметр из URL
     public Book getBookById(@PathVariable int id) {
        return bookService.getBookById(id);
    }
}

Service
package com.bookmanagmentapp.bookmanagmentapplication.service;

import com.bookmanagmentapp.bookmanagmentapplication.dao.BookDao;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookService {
    private final BookDao bookDao;

    public List<Book> getBooksByAuthor(String author) { // Передаём запрос в DAO для получения списка книг
        return bookDao.findByAuthor(author);
    }

    public Book getBookById(int id) { // Передаём запрос в DAO для получения книги
        return bookDao.findById(id);
    }
}

*/