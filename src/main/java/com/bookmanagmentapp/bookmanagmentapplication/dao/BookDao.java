package com.bookmanagmentapp.bookmanagmentapplication.dao;

import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import java.util.List;
import org.springframework.stereotype.Repository;

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
                .orElse(null);
    }
}
