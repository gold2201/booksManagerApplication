package com.bookmanagmentapp.bookmanagmentapplication.dto;

import java.util.List;
import lombok.Data;

@Data
public class BookAuthorsUpdateDto {
    private String oldAuthorName;
    private String newAuthorName;
}
