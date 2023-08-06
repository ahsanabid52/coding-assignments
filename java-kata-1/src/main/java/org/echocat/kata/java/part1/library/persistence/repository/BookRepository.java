package org.echocat.kata.java.part1.library.persistence.repository;

import org.echocat.kata.java.part1.library.persistence.AuthorEntity;
import org.echocat.kata.java.part1.library.persistence.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    BookEntity findByIsbn(String isbn);

    List<BookEntity> findAllByAuthors(AuthorEntity author);

}