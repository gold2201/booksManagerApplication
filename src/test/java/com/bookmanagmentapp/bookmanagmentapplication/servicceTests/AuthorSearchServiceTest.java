package com.bookmanagmentapp.bookmanagmentapplication.servicceTests;

import com.bookmanagmentapp.bookmanagmentapplication.cache.InMemoryCache;
import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.service.authorservices.AuthorSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorSearchServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private InMemoryCache<String, List<Author>> cache;

    @InjectMocks
    private AuthorSearchService authorSearchService;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setName("Test Author");
    }

    @Test
    void getAllAuthors_shouldReturnList() {
        when(authorRepository.findAll()).thenReturn(List.of(author));

        List<Author> result = authorSearchService.getAllAuthors();

        assertEquals(1, result.size());
        assertEquals("Test Author", result.get(0).getName());
    }

    @Test
    void getAuthorById_whenExists_shouldReturnAuthor() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Author result = authorSearchService.getAuthorById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getAuthorById_whenNotExists_shouldThrowException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authorSearchService.getAuthorById(1L)
        );

        assertEquals("Автор не найден", exception.getReason());
    }

    @Test
    void getAuthorsByBookTitle_whenCached_shouldReturnFromCache() {
        when(cache.get("Some Book")).thenReturn(List.of(author));

        List<Author> result = authorSearchService.getAuthorsByBookTitle("Some Book");

        assertEquals(1, result.size());
        verify(authorRepository, never()).findAuthorsByBookTitle(anyString());
    }

    @Test
    void getAuthorsByBookTitle_whenNotCachedAndFound_shouldQueryDatabaseAndPutToCache() {
        when(cache.get("Some Book")).thenReturn(null);
        when(authorRepository.findAuthorsByBookTitle("Some Book")).thenReturn(List.of(author));

        List<Author> result = authorSearchService.getAuthorsByBookTitle("Some Book");

        assertEquals(1, result.size());
        verify(cache).put(eq("Some Book"), any());
    }

    @Test
    void getAuthorsByBookTitle_whenNotCachedAndNotFound_shouldThrowException() {
        when(cache.get("Unknown Book")).thenReturn(null);
        when(authorRepository.findAuthorsByBookTitle("Unknown Book")).thenReturn(List.of());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authorSearchService.getAuthorsByBookTitle("Unknown Book")
        );

        assertEquals("Авторы для книги не найдены", exception.getReason());
    }
}