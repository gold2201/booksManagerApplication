package com.bookmanagmentapp.bookmanagmentapplication.dto;

import com.bookmanagmentapp.bookmanagmentapplication.model.Chapter;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChapterDto {
    private Long id;

    @NotBlank(message = "Название главы не может быть пустым")
    private String title;

    @NotBlank(message = "Содержание главы не может быть пустым")
    private String content;

    public static ChapterDto fromEntity(Chapter chapter) {
        return new ChapterDto(
                chapter.getId(),
                chapter.getTitle(),
                chapter.getContent()
        );
    }

    public Chapter toEntity() {
        Chapter chapter = new Chapter();
        chapter.setTitle(this.title);
        chapter.setContent(this.content);
        return chapter;
    }
}

