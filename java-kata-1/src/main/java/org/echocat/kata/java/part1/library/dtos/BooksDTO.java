package org.echocat.kata.java.part1.library.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BooksDTO {


    private String title;

    private String isbn;

    private List<AuthorDTO> authors;

    private String description;

    private String publishedAt;

}
