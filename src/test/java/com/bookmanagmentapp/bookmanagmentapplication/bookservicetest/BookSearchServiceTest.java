package com.bookmanagmentapp.bookmanagmentapplication.bookservicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import com.bookmanagmentapp.bookmanagmentapplication.cache.InMemoryCache;
import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookDto;
import com.bookmanagmentapp.bookmanagmentapplication.exceptions.BookNotFoundException;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import com.bookmanagmentapp.bookmanagmentapplication.service.bookservices.BookSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookSearchServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private InMemoryCache<String, List<Book>> cache;

    @InjectMocks
    private BookSearchService bookSearchService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
    }

    @Test
    void getBookById_whenBookExists_shouldReturnBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto foundBook = bookSearchService.getBookById(1L);

        assertNotNull(foundBook);
        assertEquals(1L, foundBook.getId());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_whenBookDoesNotExist_shouldThrowException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BookNotFoundException.class, () -> bookSearchService.getBookById(1L));

        assertEquals("Книга с ID 1 не найдена", exception.getMessage());
    }

    @Test
    void getAllBooks_shouldReturnBookList() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookDto> books = bookSearchService.getAllBooks();

        assertEquals(1, books.size());
        assertEquals("Test Book", books.get(0).getTitle());
    }

    @Test
    void getBooksByAuthorName_whenCached_shouldReturnFromCache() {
        when(cache.get("Author Name")).thenReturn(List.of(book));

        List<BookDto> books = bookSearchService.getBooksByAuthorName("Author Name");

        assertEquals(1, books.size());
        verify(bookRepository, never()).findByAuthorName(any());
    }

    @Test
    void getBooksByAuthorName_whenNotCached_shouldQueryDatabaseAndCacheResult() {
        when(cache.get("Author Name")).thenReturn(null);
        when(bookRepository.findByAuthorName("Author Name")).thenReturn(List.of(book));

        List<BookDto> books = bookSearchService.getBooksByAuthorName("Author Name");

        assertEquals(1, books.size());
        verify(cache).put(eq("Author Name"), any());
    }

    @Test
    void getBooksByAuthorName_whenNotFound_shouldThrowException() {
        when(cache.get("Unknown Author")).thenReturn(null);
        when(bookRepository.findByAuthorName("Unknown Author")).thenReturn(List.of());

        Exception exception = assertThrows(BookNotFoundException.class, () -> bookSearchService.getBooksByAuthorName("Unknown Author"));

        assertEquals("Книги по автору Unknown Author не найдены", exception.getMessage());
    }
}
