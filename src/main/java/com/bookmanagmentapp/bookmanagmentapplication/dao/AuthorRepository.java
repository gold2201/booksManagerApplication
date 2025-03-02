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
    @EntityGraph(attributePaths = {"primaryBooks", "books"})
    Optional<Author> findByName(String name);

    @Query("SELECT DISTINCT a FROM Author a " +
            "LEFT JOIN a.books b " +
            "LEFT JOIN a.primaryBooks pb " +
            "WHERE b.title = :bookTitle OR pb.title = :bookTitle")
    List<Author> findAuthorsByBookTitle(@Param("bookTitle") String bookTitle);
}
