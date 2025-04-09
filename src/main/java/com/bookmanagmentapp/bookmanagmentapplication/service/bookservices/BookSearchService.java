package com.bookmanagmentapp.bookmanagmentapplication.service.bookservices;

import com.bookmanagmentapp.bookmanagmentapplication.cache.InMemoryCache;
import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookDto;
import com.bookmanagmentapp.bookmanagmentapplication.exceptions.BookNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookSearchService {
    private final BookRepository bookRepository;
    private final InMemoryCache<String, List<BookDto>> cache;
    private final Logger logger = LoggerFactory.getLogger(BookSearchService.class);

    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(BookDto::fromEntity)
                .orElseThrow(() -> new BookNotFoundException("Книга с ID " + id + " не найдена"));
    }

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDto::fromEntity)
                .toList();
    }

    public List<BookDto> getBooksByAuthorName(String authorName) {
        List<BookDto> cachedBooks = cache.get(authorName);
        if (cachedBooks != null) {
            logger.info("✅ Данные получены из кэша: (authorName: [hidden])");
            return cachedBooks;
        }

        logger.info("⏳ Данные не найдены в кэше, идем в БД: (authorName: [hidden])");
        List<BookDto> books = bookRepository.findByAuthorName(authorName).stream()
                .map(BookDto::fromEntity)
                .toList();

        if (books.isEmpty()) {
            throw new BookNotFoundException("Книги по автору " + authorName + " не найдены");
        }

        cache.put(authorName, books);
        logger.info("✅ Данные добавлены в кэш: (authorName: [hidden])");
        return books;
    }
}


