package com.bookmanagmentapp.bookmanagmentapplication.service;

import com.bookmanagmentapp.bookmanagmentapplication.dao.ChapterRepository;
import com.bookmanagmentapp.bookmanagmentapplication.model.Chapter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
@Validated
public class ChapterService {
    private final ChapterRepository chapterRepository;

    public Chapter getChapterById(@Min(1) Long id) {
        return chapterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Глава не найдена"));
    }

    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    @Transactional
    public Chapter updateChapter(@Min(1) Long id, @Valid Chapter updatedChapter) {
        Chapter existingChapter = chapterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Глава не найдена"));
        existingChapter.setTitle(updatedChapter.getTitle());
        existingChapter.setContent(updatedChapter.getContent());
        return chapterRepository.save(existingChapter);
    }

    public void deleteChapter(@Min(1) Long id) {
        chapterRepository.deleteById(id);
    }
}

