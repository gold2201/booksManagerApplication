package com.bookmanagmentapp.bookmanagmentapplication.dto;

import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String title;
    private Set<AuthorDto> authors;

    public static BookDto fromEntity(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthors().stream().map(AuthorDto::fromEntity).collect(Collectors.toSet())
        );
    }

    public Book toEntity() {
        Book book = new Book();
        book.setId(this.id);
        book.setTitle(this.title);
        book.setAuthors(
                this.authors.stream()
                        .map(AuthorDto::toEntity)
                        .collect(Collectors.toSet())
        );
        return book;
    }
}
