package com.bookmanagmentapp.bookmanagmentapplication.service;

import com.bookmanagmentapp.bookmanagmentapplication.dao.AuthorRepository;
import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор не найден"));
    }

    public List<Author> getAuthorsByBookTitle(String bookTitle) {
        List<Author> authors = authorRepository.findAuthorsByBookTitle(bookTitle);
        if (authors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Авторы для книги не найдены");
        }
        return authors;
    }

    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        // Найдем автора
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор не найден"));

        // Для книг, где автор является основным, сбросим primaryAuthor (установим в null)
        // Предполагаем, что у автора есть коллекция primaryBooks
        for (Book book : author.getPrimaryBooks()) {
            book.setPrimaryAuthor(null);
            //  можно сохранить книгу, но если каскад настроен, Hibernate сделает это автоматически.
        }

        // Для Many-to-Many связи удаляем автора из списка соавторов у каждой книги
        for (Book book : author.getBooks()) {
            book.getAuthors().remove(author);
            // При необходимости сохранить книгу:
            // bookRepository.save(book);
        }

        // Теперь можно удалить автора
        authorRepository.delete(author);
    }
}

