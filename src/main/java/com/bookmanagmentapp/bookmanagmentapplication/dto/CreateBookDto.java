package com.bookmanagmentapp.bookmanagmentapplication.dto;

import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDto {
    @NotBlank(message = "Название книги не может быть пустым")
    private String title;

    @NotEmpty(message = "У книги должен быть хотя бы один автор")
    @Valid
    private Set<AuthorDto> authors;

    public Book toEntity() {
        return new Book(
                null,  // ID будет сгенерирован автоматически
                title,
                authors.stream().map(AuthorDto::toEntity).collect(Collectors.toSet()),
                new HashSet<>()  // Пустой набор глав
        );
    }
}
