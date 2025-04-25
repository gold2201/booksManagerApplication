package com.bookmanagmentapp.bookmanagmentapplication.service.authorservices;

import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.dto.AuthorDto;
import com.bookmanagmentapp.bookmanagmentapplication.dto.CreateAuthorDto;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class AuthorDeleteService {
    private final AuthorRepository authorRepository;

    @Transactional
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор не найден"));

        for (Book book : author.getBooks()) {
            book.getAuthors().remove(author);
        }

        authorRepository.delete(author);
    }

    @Transactional
    public AuthorDto register(CreateAuthorDto dto) {
        // Смотрим, есть ли уже автор
        Optional<Author> existingOpt = authorRepository.findByName(dto.getName());
        if (existingOpt.isPresent()) {
            // Просто возвращаем DTO существующего
            return AuthorDto.fromEntity(existingOpt.get());
        }
        // Иначе создаём нового
        Author a = new Author(dto.getName());
        // TODO: хранить и хешировать пароль
        Author saved = authorRepository.save(a);
        return AuthorDto.fromEntity(saved);
    }
}
