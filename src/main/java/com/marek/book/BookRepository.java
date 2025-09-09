package com.marek.book;

import com.marek.shared.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthor(String author);
    @Query("SELECT b FROM Book b JOIN b.genres g " +
            "WHERE g IN :genres " +
            "GROUP BY b " +
            "HAVING COUNT(DISTINCT g) = :size")
    List<Book> findByAllGenres(@Param("genres") List<BookGenre> genres,
                               @Param("size") long size);
    Optional<Book> findByName(String name);

    List<Book> findByIsLostTrue();
}
