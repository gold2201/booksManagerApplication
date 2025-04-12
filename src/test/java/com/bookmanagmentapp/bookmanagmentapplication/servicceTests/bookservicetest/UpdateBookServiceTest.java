package com.bookmanagmentapp.bookmanagmentapplication.servicceTests.bookservicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookTitleUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.exceptions.AuthorNotFoundException;
import com.bookmanagmentapp.bookmanagmentapplication.exceptions.BookNotFoundException;
import com.bookmanagmentapp.bookmanagmentapplication.exceptions.InvalidBookOperationException;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import com.bookmanagmentapp.bookmanagmentapplication.service.bookservices.UpdateBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class UpdateBookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private UpdateBookService updateBookService;

    private Book book;

    @BeforeEach
    void setUp() {
        Author oldAuthor = new Author("Old Author");

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthors(Set.of(oldAuthor));
    }

    @Test
    void updateAuthor_whenBookNotFound_shouldThrowException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BookNotFoundException.class,
                () -> updateBookService.updateAuthor(1L, "Old Author", "New Author"));

        assertEquals("Книга с ID 1 не найдена", exception.getMessage());
    }

    @Test
    void updateAuthor_whenOldAuthorNotFound_shouldThrowException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findByName("Old Author")).thenReturn(Optional.empty());

        Exception exception = assertThrows(AuthorNotFoundException.class,
                () -> updateBookService.updateAuthor(1L, "Old Author", "New Author"));

        assertEquals("Автор Old Author не найден", exception.getMessage());
    }

    @Test
    void updateBookTitle_whenBookExists_shouldUpdateTitle() {
        BookTitleUpdateDto dto = new BookTitleUpdateDto();
        dto.setTitle("New Title");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book updatedBook = updateBookService.updateBookTitle(1L, dto).toEntity();

        assertEquals("New Title", updatedBook.getTitle());
    }

    @Test
    void updateBookTitle_whenBookNotFound_shouldThrowException() {
        BookTitleUpdateDto dto = new BookTitleUpdateDto();
        dto.setTitle("New Title");

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BookNotFoundException.class,
                () -> updateBookService.updateBookTitle(1L, dto));

        assertEquals("Книга с ID 1 не найдена", exception.getMessage());
    }

    @Test
    void updateBookTitle_whenTitleIsEmpty_shouldThrowException() {
        BookTitleUpdateDto dto = new BookTitleUpdateDto();
        dto.setTitle("   ");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Exception exception = assertThrows(InvalidBookOperationException.class,
                () -> updateBookService.updateBookTitle(1L, dto));

        assertEquals("Название книги не может быть пустым", exception.getMessage());
    }
}

