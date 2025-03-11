package com.bookmanagmentapp.bookmanagmentapplication.controller;

import com.bookmanagmentapp.bookmanagmentapplication.model.Chapter;
import com.bookmanagmentapp.bookmanagmentapplication.service.ChapterService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chapters")
@AllArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @GetMapping("/{id}")
    public Chapter getChapterById(@PathVariable Long id) {
        return chapterService.getChapterById(id);
    }

    @GetMapping
    public List<Chapter> getAllChapters() {
        return chapterService.getAllChapters();
    }

    @PutMapping("/{id}")
    public Chapter updateChapter(@PathVariable Long id, @RequestBody Chapter chapter) {
        return chapterService.updateChapter(id, chapter);
    }

    @DeleteMapping("/{id}")
    public void deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
    }
}

