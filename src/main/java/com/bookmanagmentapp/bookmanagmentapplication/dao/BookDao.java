package com.bookmanagmentapp.bookmanagmentapplication.dao;

import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BookDao {
    private final List<Book> books = List.of(// пока что имитируем бд
            new Book(1, "Война и мир", "Лев Толстой"),
            new Book(2, "Анна Каренина", "Лев Толстой"),
            new Book(3, "Преступление и наказание", "Федор Достоевский")
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
                .orElse(null);
    }
}
