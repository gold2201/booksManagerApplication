package com.bookmanagmentapp.bookmanagmentapplication.dao;

import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = {"authors"}) // Жадная загрузка авторов
    Optional<Book> findById(Long id);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.name = :authorName")
    @EntityGraph(attributePaths = {"authors"}) // Жадная загрузка авторов
    List<Book> findByAuthorName(@Param("authorName") String authorName);
}

