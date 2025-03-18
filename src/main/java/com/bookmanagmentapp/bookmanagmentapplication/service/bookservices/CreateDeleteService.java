package com.bookmanagmentapp.bookmanagmentapplication.service.bookservices;

import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateDeleteService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

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

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
