package com.bookmanagmentapp.bookmanagmentapplication.service.bookservices;

import com.bookmanagmentapp.bookmanagmentapplication.cache.InMemoryCache;
import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class BookSearchService {
    private final BookRepository bookRepository;
    private final InMemoryCache<String, List<Book>> cache;
    private final Logger logger = LoggerFactory.getLogger(BookSearchService.class);

    private static final String BOOK_NOT_FOUND = "Книга не найдена";

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND));
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByAuthorName(String authorName) {
        List<Book> cachedBooks = cache.get(authorName);
        if (cachedBooks != null) {
            logger.info("✅ Данные получены из кэша: (authorName: [hidden])");
            return cachedBooks;
        }

        logger.info("⏳ Данные не найдены в кэше, идем в БД: (authorName: [hidden])");
        List<Book> books = bookRepository.findByAuthorName(authorName);

        if (books.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книги по данному автору не найдены");
        }

        cache.put(authorName, books);
        logger.info("✅ Данные добавлены в кэш: (authorName: [hidden])");
        return books;
    }
}


