package com.bookmanagmentapp.bookmanagmentapplication.servicceTests;

import com.bookmanagmentapp.bookmanagmentapplication.model.Chapter;
import com.bookmanagmentapp.bookmanagmentapplication.dao.ChapterRepository;
import com.bookmanagmentapp.bookmanagmentapplication.service.ChapterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

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
    void deleteChapter_shouldCallDeleteById() {
        chapterService.deleteChapter(1L);
        verify(chapterRepository).deleteById(1L);
    }
}
