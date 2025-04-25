package com.bookmanagmentapp.bookmanagmentapplication.service.bookservices;

import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dao.BookRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookDto;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.dto.ChapterDto;
import com.bookmanagmentapp.bookmanagmentapplication.exceptions.BookNotFoundException;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import com.bookmanagmentapp.bookmanagmentapplication.model.Chapter;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateBookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public BookDto updateBookPartial(Long bookId, BookUpdateDto dto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Книга с ID " + bookId + " не найдена"));

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            book.setTitle(dto.getTitle());
        }

        if (dto.getAuthors() != null && !dto.getAuthors().isEmpty()) {
            Set<Author> authors = dto.getAuthors().stream()
                    .map(a -> authorRepository.findByName(a.getName())
                            .orElseGet(() -> authorRepository.save(new Author(a.getName()))))
                    .collect(Collectors.toSet());
            book.setAuthors(authors);
        }

        if (dto.getChapters() != null) {
            book.getChapters().clear(); // удалить старые главы

            Set<Chapter> updatedChapters = dto.getChapters().stream()
                    .map(ChapterDto::toEntity)
                    .collect(Collectors.toSet());

            updatedChapters.forEach(c -> c.setBook(book));
            book.getChapters().addAll(updatedChapters); // добавить новые главы
        }

        if (dto.getImageName() != null && !dto.getImageName().isBlank()) {
            book.setImagePath("images/" + dto.getImageName());
        }

        return BookDto.fromEntity(bookRepository.save(book));
    }
}
