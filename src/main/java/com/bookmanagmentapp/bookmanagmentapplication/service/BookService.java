package com.bookmanagmentapp.bookmanagmentapplication.service;

import com.bookmanagmentapp.bookmanagmentapplication.dao.BookDao;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookDao bookDao;

    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public List<Book> getBooksByAuthor(String author) { // Передаём запрос в DAO для получения списка книг
        return bookDao.findByAuthor(author);
    }

    public Book getBookById(int id) { // Передаём запрос в DAO для получения книги
        return bookDao.findById(id);
    }
}
