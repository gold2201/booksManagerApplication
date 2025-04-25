package com.bookmanagmentapp.bookmanagmentapplication.dto;

import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateBookDto {
    @NotBlank(message = "Название книги не может быть пустым")
    private String title;

    @NotEmpty(message = "У книги должен быть хотя бы один автор")
    @Valid
    private Set<AuthorDto> authors;

    @Valid
    private Set<ChapterDto> chapters = new HashSet<>();

    @NotBlank(message = "Имя файла картинки не может быть пустым")
    private String imageName;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .authors(authors.stream().map(AuthorDto::toEntity).collect(Collectors.toSet()))
                .chapters(chapters != null ?
                        chapters.stream().map(ChapterDto::toEntity).collect(Collectors.toSet())
                        : new HashSet<>())
                .imagePath(imageName)
                .build();
    }
}


