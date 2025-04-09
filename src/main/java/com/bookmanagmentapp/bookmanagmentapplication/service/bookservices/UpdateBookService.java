package com.bookmanagmentapp.bookmanagmentapplication.service.bookservices;

import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookDto;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookTitleUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.exceptions.AuthorNotFoundException;
import com.bookmanagmentapp.bookmanagmentapplication.exceptions.BookNotFoundException;
import com.bookmanagmentapp.bookmanagmentapplication.exceptions.InvalidBookOperationException;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateBookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public BookDto updateAuthor(Long bookId, String oldAuthorName, String newAuthorName) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Книга с ID " + bookId + " не найдена"));

        Author oldAuthor = authorRepository.findByName(oldAuthorName)
                .orElseThrow(() -> new AuthorNotFoundException("Автор " + oldAuthorName + " не найден"));

        if (!existingBook.getAuthors().contains(oldAuthor)) {
            throw new InvalidBookOperationException("Автор " + oldAuthorName + " не связан с данной книгой");
        }

        Author newAuthor = authorRepository.findByName(newAuthorName)
                .orElseGet(() -> authorRepository.save(new Author(newAuthorName)));

        existingBook.getAuthors().remove(oldAuthor);
        existingBook.getAuthors().add(newAuthor);

        Book updatedBook = bookRepository.save(existingBook);
        return BookDto.fromEntity(updatedBook);
    }

    @Transactional
    public BookDto updateBookTitle(Long id, BookTitleUpdateDto dto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Книга с ID " + id + " не найдена"));

        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new InvalidBookOperationException("Название книги не может быть пустым");
        }

        existingBook.setTitle(dto.getTitle());
        Book updatedBook = bookRepository.save(existingBook);

        return BookDto.fromEntity(updatedBook);
    }
}
