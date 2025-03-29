package com.bookmanagmentapp.bookmanagmentapplication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookTitleUpdateDto {
    @NotBlank(message = "Название книги не может быть пустым")
    private String title;
}
