package com.bookmanagmentapp.bookmanagmentapplication.servicceTests;

import com.bookmanagmentapp.bookmanagmentapplication.model.Chapter;
import com.bookmanagmentapp.bookmanagmentapplication.dao.ChapterRepository;
import com.bookmanagmentapp.bookmanagmentapplication.service.ChapterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChapterServiceTest {

    private ChapterRepository chapterRepository;
    private ChapterService chapterService;

    @BeforeEach
    void setUp() {
        chapterRepository = mock(ChapterRepository.class);
        chapterService = new ChapterService(chapterRepository);
    }

    @Test
    void getChapterById_shouldReturnChapter_ifExists() {
        Chapter chapter = new Chapter();
        chapter.setId(1L);
        chapter.setTitle("Title");
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        Chapter result = chapterService.getChapterById(1L);

        assertThat(result).isEqualTo(chapter);
        verify(chapterRepository).findById(1L);
    }

    @Test
    void getChapterById_shouldThrow_ifNotFound() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> chapterService.getChapterById(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Глава не найдена");
    }

    @Test
    void getAllChapters_shouldReturnAll() {
        List<Chapter> chapters = Arrays.asList(
                new Chapter(1L, "Title 1", "Content 1"),
                new Chapter(2L, "Title 2", "Content 2")
        );
        when(chapterRepository.findAll()).thenReturn(chapters);

        List<Chapter> result = chapterService.getAllChapters();

        assertThat(result).hasSize(2);
        verify(chapterRepository).findAll();
    }

    @Test
    void updateChapter_shouldUpdateAndReturnChapter() {
        Chapter existing = new Chapter(1L, "Old Title", "Old Content");
        Chapter updated = new Chapter(null, "New Title", "New Content");

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(chapterRepository.save(any(Chapter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Chapter result = chapterService.updateChapter(1L, updated);

        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getContent()).isEqualTo("New Content");

        verify(chapterRepository).save(existing);
    }

    @Test
    void updateChapter_shouldThrow_ifChapterNotFound() {
        when(chapterRepository.findById(42L)).thenReturn(Optional.empty());

        Chapter update = new Chapter(null, "Title", "Content");

        assertThatThrownBy(() -> chapterService.updateChapter(42L, update))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Глава не найдена");
    }

    @Test
    void deleteChapter_shouldCallDeleteById() {
        chapterService.deleteChapter(1L);
        verify(chapterRepository).deleteById(1L);
    }
}
