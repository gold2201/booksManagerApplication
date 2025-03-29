package com.bookmanagmentapp.bookmanagmentapplication.service.authorservices;

import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import jakarta.transaction.Transactional;
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
}
