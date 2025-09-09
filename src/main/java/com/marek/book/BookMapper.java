package com.marek.book;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {
    BookDTO toDTO(Book book);
    Book toEntity(BookDTO bookDTO);
    List<BookDTO> toDTO(List<Book> bookList);
    List<Book> toEntity(List<BookDTO> bookDTOList);
}
