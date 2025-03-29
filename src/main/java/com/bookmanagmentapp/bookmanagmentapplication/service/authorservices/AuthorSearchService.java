package com.bookmanagmentapp.bookmanagmentapplication.service.authorservices;

import com.bookmanagmentapp.bookmanagmentapplication.cache.InMemoryCache;
import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class AuthorSearchService {
    private final AuthorRepository authorRepository;
    private final InMemoryCache<String, List<Author>> cache;
    private final Logger logger = LoggerFactory.getLogger(AuthorSearchService.class);

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор не найден"));
    }

    public List<Author> getAuthorsByBookTitle(String bookTitle) {
        List<Author> cachedAuthors = cache.get(bookTitle);
        if (cachedAuthors != null) {
            logger.info("✅ Данные для книги из кэша (bookTitle: [hidden])");
            return cachedAuthors;
        }

        logger.info("⏳ Данные не найдены в кэше, идем в БД (bookTitle: [hidden])");
        List<Author> authors = authorRepository.findAuthorsByBookTitle(bookTitle);

        if (authors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Авторы для книги не найдены");
        }

        cache.put(bookTitle, authors);
        logger.info("✅ Данные добавлены в кэш для книги (bookTitle: [hidden])");
        return authors;
    }
}

