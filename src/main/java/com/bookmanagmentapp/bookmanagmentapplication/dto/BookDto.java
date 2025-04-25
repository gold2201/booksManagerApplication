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
    private String imagePath;  // <-- добавляем это!
    private Set<AuthorDto> authors;
    private Set<ChapterDto> chapters;

    // Преобразование сущности в DTO
    public static BookDto fromEntity(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getImagePath(),  // <-- сюда тоже добавляем
                book.getAuthors().stream().map(AuthorDto::fromEntity).collect(Collectors.toSet()),
                book.getChapters().stream().map(ChapterDto::fromEntity).collect(Collectors.toSet())
        );
    }

    // Преобразование DTO обратно в сущность
    public Book toEntity() {
        if (this.title == null || this.title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (this.authors == null || this.authors.isEmpty()) {
            throw new IllegalArgumentException("Authors cannot be empty");
        }

        Book book = new Book();
        book.setId(this.id);
        book.setTitle(this.title);
        book.setImagePath(this.imagePath);  // <-- добавляем сюда
        book.setAuthors(
                this.authors.stream()
                        .map(AuthorDto::toEntity)
                        .collect(Collectors.toSet())
        );
        book.setChapters(
                this.chapters.stream()
                        .map(ChapterDto::toEntity)
                        .collect(Collectors.toSet())
        );
        return book;
    }
}



