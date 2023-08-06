package org.echocat.kata.java.part1.library.persistence.repository;

import org.echocat.kata.java.part1.library.persistence.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    AuthorEntity findByEmail(String email);
}