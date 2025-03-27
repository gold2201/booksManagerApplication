package com.bookmanagmentapp.bookmanagmentapplication.service.bookservices;

import com.bookmanagmentapp.bookmanagmentapplication.cache.InMemoryCache;
import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.exceptions.BookNotFoundException;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
public class BookSearchService {
    private final BookRepository bookRepository;
    private final InMemoryCache<String, List<Book>> cache;
    private final Logger logger = LoggerFactory.getLogger(BookSearchService.class);

    public Book getBookById(@Min(1) Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Книга с ID " + id + " не найдена"));
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByAuthorName(@NotBlank String authorName) {
        List<Book> cachedBooks = cache.get(authorName);
        if (cachedBooks != null) {
            logger.info("✅ Данные получены из кэша: (authorName: [hidden])");
            return cachedBooks;
        }

        logger.info("⏳ Данные не найдены в кэше, идем в БД: (authorName: [hidden])");
        List<Book> books = bookRepository.findByAuthorName(authorName);

        if (books.isEmpty()) {
            throw new BookNotFoundException("Книги по автору " + authorName + " не найдены");
        }

        cache.put(authorName, books);
        logger.info("✅ Данные добавлены в кэш: (authorName: [hidden])");
        return books;
    }
}


