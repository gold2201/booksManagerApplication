package com.bookmanagmentapp.bookmanagmentapplication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAuthorDto {
    @NotBlank(message = "Имя автора не может быть пустым")
    private String name;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}

