package com.bookmanagmentapp.bookmanagmentapplication.service;

import com.bookmanagmentapp.bookmanagmentapplication.InMemoryCache;
import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookTitleUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final InMemoryCache<String, List<Book>> cache;
    private final Logger logger = LoggerFactory.getLogger(BookService.class);

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

    @Transactional
    public Book saveBook(Book book) {
        Set<Author> attachedAuthors = new LinkedHashSet<>();

        for (Author author : new HashSet<>(book.getAuthors())) {
            Author persistentAuthor = authorRepository.findByName(author.getName())
                    .orElseGet(() -> authorRepository.save(new Author(author.getName())));
            attachedAuthors.add(persistentAuthor);
        }

        book.setAuthors(attachedAuthors);

        return bookRepository.save(book);
    }

    @Transactional
    public Book updateAuthor(Long bookId, String oldAuthorName, String newAuthorName) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND));

        Author oldAuthor = authorRepository.findByName(oldAuthorName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет старого автора"));

        if (!existingBook.getAuthors().contains(oldAuthor)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Этого автора нет у данной книги");
        }

        Author newAuthor = authorRepository.findByName(newAuthorName)
                .orElseGet(() -> authorRepository.save(new Author(newAuthorName)));

        existingBook.getAuthors().remove(oldAuthor);
        existingBook.getAuthors().add(newAuthor);

        return bookRepository.save(existingBook);
    }

    @Transactional
    public Book updateBookTitle(Long id, BookTitleUpdateDto dto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND));
        existingBook.setTitle(dto.getTitle());
        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}


