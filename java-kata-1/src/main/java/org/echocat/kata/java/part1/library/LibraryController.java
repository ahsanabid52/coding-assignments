package org.echocat.kata.java.part1.library;

import lombok.AllArgsConstructor;
import org.echocat.kata.java.part1.library.dtos.BooksDTO;
import org.echocat.kata.java.part1.library.persistence.BookEntity;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/api/library/")
public class LibraryController {

    private final LibraryService libraryService;
    private final ModelMapper modelMapper;

    @PostMapping(value = "/parse")
    @ResponseStatus(HttpStatus.CREATED)
    public void parseTextFiles() throws FileNotFoundException {
        libraryService.parseTextFiles();
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<BooksDTO>> getAllBooks() {
        return ResponseEntity.ok(libraryService.getAllBooks().stream().map(this::convertToDto).
                collect(Collectors.toList()));
    }

    @GetMapping(value = "/find/isbn/{isbn}")
    public ResponseEntity<BooksDTO> getBooksOrMagazinesByIsbn(@PathVariable(value = "isbn") String isbn) {
        return ResponseEntity.ok(convertToDto(libraryService.findAllByIsbn(isbn)));
    }

    @GetMapping(value = "/find/author/{author}")
    public ResponseEntity<List<BooksDTO>> getAllBooksAndMagazinesByAuthor(@PathVariable(value = "author") String author) {
        return ResponseEntity.ok(libraryService.findAllByAuthor(author).stream().map(this::convertToDto).
                collect(Collectors.toList()));
    }

    @GetMapping(value = "/all/sorted")
    public ResponseEntity<List<BooksDTO>> getAllBooksSortedByTitle() {
        return ResponseEntity.ok(libraryService.getAllBooksSorted().stream().map(this::convertToDto).
                collect(Collectors.toList()));
    }

    private BooksDTO convertToDto(BookEntity book) {
        return modelMapper.map(book, BooksDTO.class);
    }
}