package com.bookmanagmentapp.bookmanagmentapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book { // модель книги
    private int id;
    private String title;
    private String author;
}
