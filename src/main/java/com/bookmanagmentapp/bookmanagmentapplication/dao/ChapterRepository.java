package com.bookmanagmentapp.bookmanagmentapplication.dao;

import com.bookmanagmentapp.bookmanagmentapplication.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
}
