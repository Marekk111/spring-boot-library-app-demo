package com.marek.book;

import com.marek.shared.BookGenre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Long id;
    private String author;
    private String name;
    private String description;
    private List<BookGenre> genres;
}
