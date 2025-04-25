package com.bookmanagmentapp.bookmanagmentapplication.dto;

import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorDto {
    private Long id;

    @NotBlank(message = "Имя автора не может быть пустым")
    private String name;

    public static AuthorDto fromEntity(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }

    public Author toEntity() {
        return new Author(id, name, new HashSet<>());
    }
}
