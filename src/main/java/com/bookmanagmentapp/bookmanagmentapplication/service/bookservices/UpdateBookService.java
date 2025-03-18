package com.bookmanagmentapp.bookmanagmentapplication.service.bookservices;

import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookTitleUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UpdateBookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private static final String BOOK_NOT_FOUND = "Книга не найдена";

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
}
