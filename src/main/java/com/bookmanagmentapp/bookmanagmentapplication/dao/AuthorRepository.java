package com.bookmanagmentapp.bookmanagmentapplication.dao;

import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT DISTINCT a FROM Author a JOIN a.books b WHERE b.title = :bookTitle")
    List<Author> findAuthorsByBookTitle(@Param("bookTitle") String bookTitle);
}
