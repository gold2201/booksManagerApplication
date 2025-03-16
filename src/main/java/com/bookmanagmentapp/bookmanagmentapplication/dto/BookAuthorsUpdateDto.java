package com.bookmanagmentapp.bookmanagmentapplication.dto;

import lombok.Data;

@Data
public class BookAuthorsUpdateDto {
    private String oldAuthorName;
    private String newAuthorName;
}
