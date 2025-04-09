package com.bookmanagmentapp.bookmanagmentapplication.bookservicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;
import com.bookmanagmentapp.bookmanagmentapplication.cache.InMemoryCache;
import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.exceptions.BookNotFoundException;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import com.bookmanagmentapp.bookmanagmentapplication.service.bookservices.CreateDeleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class CreateDeleteServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private InMemoryCache<String, Book> inMemoryCache;

    @InjectMocks
    private CreateDeleteService createDeleteService;

    private Book book;
    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author("Test Author");
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthors(Set.of(author));
    }

    @Test
    void saveBook_NewAuthor_ShouldSaveSuccessfully() {
        // Если автора с данным именем нет в базе, возвращаем Optional.empty()
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.empty());
        // При сохранении нового автора возвращаем нашего автора
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        // При сохранении книги возвращаем нашу книгу
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Вызываем метод напрямую, передавая сущность Book (без преобразования через DTO)
        Book savedBook = createDeleteService.saveBook(book);

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        // Ожидаем, что новый автор будет сохранён (1 раз)
        verify(authorRepository, times(1)).save(any(Author.class));
        // И книга сохранена 1 раз
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void saveBook_ExistingAuthor_ShouldUseExistingAuthor() {
        // Если автор уже есть в базе, возвращаем его
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Вызываем метод напрямую с сущностью Book
        Book savedBook = createDeleteService.saveBook(book);

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        // Ожидаем, что метод сохранения автора не будет вызван, так как автор уже существует
        verify(authorRepository, never()).save(any(Author.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }


    @Test
    void deleteBook_BookExists_ShouldDeleteSuccessfully() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(1L);
        doNothing().when(inMemoryCache).remove(book.getTitle());

        assertDoesNotThrow(() -> createDeleteService.deleteBook(1L));
        verify(inMemoryCache, times(1)).remove(book.getTitle());
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_BookNotFound_ShouldThrowException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> createDeleteService.deleteBook(1L));
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void saveBooks_ShouldSaveMultipleBooks() {
        List<Book> books = List.of(book, new Book());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        List<Book> savedBooks = createDeleteService.saveBooks(books);

        assertEquals(2, savedBooks.size());
        verify(bookRepository, times(2)).save(any(Book.class));
    }
}

