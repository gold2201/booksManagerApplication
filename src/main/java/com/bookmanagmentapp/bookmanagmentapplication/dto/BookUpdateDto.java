package com.bookmanagmentapp.bookmanagmentapplication.dto;

import java.util.Set;
import lombok.Data;

@Data
public class BookUpdateDto {
    private String title;
    private Set<AuthorDto> authors;
    private Set<ChapterDto> chapters;
    private String imageName;
}
