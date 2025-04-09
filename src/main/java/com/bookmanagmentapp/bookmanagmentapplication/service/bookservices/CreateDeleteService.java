package com.bookmanagmentapp.bookmanagmentapplication.service.bookservices;

import com.bookmanagmentapp.bookmanagmentapplication.cache.InMemoryCache;
import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookDto;
import com.bookmanagmentapp.bookmanagmentapplication.exceptions.BookNotFoundException;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
public class CreateDeleteService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final InMemoryCache<String, BookDto> inMemoryCache;

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
    public List<Book> saveBooks(List<Book> books) {
        return books.stream()
                .map(this::saveBook)
                .toList();
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Книга с ID " + id + " не найдена"));

        String bookTitle = book.getTitle();
        inMemoryCache.remove(bookTitle);

        bookRepository.deleteById(id);
    }
}
