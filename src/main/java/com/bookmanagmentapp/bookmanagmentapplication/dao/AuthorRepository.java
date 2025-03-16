package com.bookmanagmentapp.bookmanagmentapplication.dao;

import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @EntityGraph(attributePaths = {"books"})
    Optional<Author> findByName(String name);

    @Query(value = "SELECT DISTINCT a.* FROM author a " +
            "LEFT JOIN book_author ba ON a.id = ba.author_id " +
            "LEFT JOIN book b ON ba.book_id = b.id " +
            "WHERE b.title = :bookTitle",
            nativeQuery = true)
    List<Author> findAuthorsByBookTitle(@Param("bookTitle") String bookTitle);
}
