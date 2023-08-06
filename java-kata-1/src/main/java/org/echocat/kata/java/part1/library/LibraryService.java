package org.echocat.kata.java.part1.library;

import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.echocat.kata.java.part1.library.persistence.AuthorEntity;
import org.echocat.kata.java.part1.library.persistence.BookEntity;
import org.echocat.kata.java.part1.library.persistence.repository.AuthorRepository;
import org.echocat.kata.java.part1.library.persistence.repository.BookRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
@Transactional
public class LibraryService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public void parseTextFiles() {
        authorRepository.saveAll(parseAuthors("authors.csv"));
        List<BookEntity> books = parseBooks("books.csv");
        List<BookEntity> magazines = parseMagazines("magazines.csv");
        books.addAll(magazines);
        bookRepository.saveAll(books);
    }

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookEntity findAllByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);

    }

    public List<BookEntity> findAllByAuthor(String email) {
        return bookRepository.findAllByAuthors(authorRepository.findByEmail(email));
    }

    public List<BookEntity> getAllBooksSorted() {
        return bookRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    private List<AuthorEntity> parseAuthors(String filename) {
        List<AuthorEntity> authors = new ArrayList<>();
        Resource resource = new ClassPathResource(filename);
        try (Reader reader = Files.newBufferedReader(resource.getFile().toPath(), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';'))) {
            for (CSVRecord csvRecord : csvParser) {
                authors.add(new AuthorEntity(0L, csvRecord.get(0),
                        csvRecord.get(1), csvRecord.get(2), null));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return authors;
    }


    private List<BookEntity> parseBooks(String filename) {
        List<BookEntity> books = new ArrayList<>();
        Resource resource = new ClassPathResource(filename);
        try (Reader reader = Files.newBufferedReader(resource.getFile().toPath(), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';'))) {
            for (CSVRecord csvRecord : csvParser) {
                BookEntity book = new BookEntity();
                book.setTitle(csvRecord.get(0));
                book.setIsbn(csvRecord.get(1));
                book.getAuthors().addAll(splitAndGetAuthors(csvRecord.get(2)));
                book.setDescription(csvRecord.get(3));
                books.add(book);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    private List<AuthorEntity> splitAndGetAuthors(String authors) {
        List<AuthorEntity> authorsList = new ArrayList<>();

        if (StringUtils.hasText(authors)) {
            String[] split = authors.split(",");
            for (String author : split) {
                authorsList.add(authorRepository.findByEmail(author));
            }
        }
        return authorsList;
    }


    private List<BookEntity> parseMagazines(String filename) {
        List<BookEntity> books = new ArrayList<>();
        Resource resource = new ClassPathResource(filename);
        try (Reader reader = Files.newBufferedReader(resource.getFile().toPath(), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';'))) {
            for (CSVRecord csvRecord : csvParser) {
                BookEntity book = new BookEntity();
                book.setTitle(csvRecord.get(0));
                book.setIsbn(csvRecord.get(1));
                book.getAuthors().addAll(splitAndGetAuthors(csvRecord.get(2)));
                book.setPublishedAt(csvRecord.get(3));
                books.add(book);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }
}