package com.bookmanagmentapp.bookmanagmentapplication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookAuthorsUpdateDto {
    @NotBlank(message = "Старое имя автора не может быть пустым")
    private String oldAuthorName;

    @NotBlank(message = "Новое имя автора не может быть пустым")
    private String newAuthorName;
}
