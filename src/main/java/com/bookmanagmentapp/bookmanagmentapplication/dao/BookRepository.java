package com.bookmanagmentapp.bookmanagmentapplication.dao;

import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    @EntityGraph(attributePaths = {"authors", "chapters"})
    @NonNull
    Optional<Book> findById(@NonNull Long id);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.name = :authorName")
    @EntityGraph(attributePaths = {"authors", "chapters"})
    @NonNull
    List<Book> findByAuthorName(@NonNull @Param("authorName") String authorName);

    @Override
    @EntityGraph(attributePaths = {"authors", "chapters"})
    List<Book> findAll();
}

