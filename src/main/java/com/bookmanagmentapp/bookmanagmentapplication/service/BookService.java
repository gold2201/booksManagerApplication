package com.bookmanagmentapp.bookmanagmentapplication.service;

import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookCoauthorsUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookPrimaryAuthorUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookTitleUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private static final String BOOK_NOT_FOUND = "Книга не найдена";

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND));
    }

    public List<Book> getBooksByAuthorName(String authorName) {
        List<Book> books = bookRepository.findByAuthorName(authorName);
        if (books.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книги по данному автору не найдены");
        }
        return books;
    }

    public List<Book> getBooksByPrimaryAuthor(Long authorId) {
        List<Book> books = bookRepository.findByPrimaryAuthor(authorId);
        if (books.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книги по основному автору не найдены");
        }
        return books;
    }

    @Transactional
    public Book saveBook(Book book) {
        if (book.getPrimaryAuthor() != null) {
            Author primaryAuthor = authorRepository.findByName(book.getPrimaryAuthor().getName())
                    .orElseGet(() -> authorRepository.save(new Author(book.getPrimaryAuthor().getName())));
            book.setPrimaryAuthor(primaryAuthor);
        }

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
    public Book updatePrimaryAuthor(Long id, BookPrimaryAuthorUpdateDto dto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND));
        Author primaryAuthor = authorRepository.findByName(dto.getPrimaryAuthorName())
                .orElseGet(() -> authorRepository.save(new Author(dto.getPrimaryAuthorName())));
        existingBook.setPrimaryAuthor(primaryAuthor);
        return bookRepository.save(existingBook);
    }

    @Transactional
    public Book updateCoauthors(Long id, BookCoauthorsUpdateDto dto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND));
        Set<Author> newCoauthors = dto.getCoauthorNames().stream()
                .map(name -> authorRepository.findByName(name)
                        .orElseGet(() -> authorRepository.save(new Author(name))))
                .collect(Collectors.toSet());
        existingBook.setAuthors(newCoauthors);
        return bookRepository.save(existingBook);
    }

    @Transactional
    public Book updateBookTitle(Long id, BookTitleUpdateDto dto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND));
        existingBook.setTitle(dto.getTitle());
        return bookRepository.save(existingBook);
    }

    public Author getPrimaryAuthorByBookTitle(String title) {
        return bookRepository.findPrimaryAuthorByBookTitle(title)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Книга с таким названием или основной автор не найден"));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}

