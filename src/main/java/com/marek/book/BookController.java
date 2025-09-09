package com.marek.book;

import com.marek.shared.BookGenre;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/all")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping("/admin/create")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        BookDTO createdBook = bookService.createBook(bookDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PostMapping("/admin/create-many")
    public ResponseEntity<List<BookDTO>> createManyBooks(@RequestBody List<BookDTO> bookDTOList) {
        List<BookDTO> createdBooks = bookService.createManyBooks(bookDTOList);
        return new ResponseEntity<>(createdBooks, HttpStatus.CREATED);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) throws Exception {
        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) throws Exception{
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/author")
    public ResponseEntity<List<BookDTO>> findByAuthor(@RequestBody String author) {
        return ResponseEntity.ok(bookService.findByAuthor(author));
    }

    @GetMapping("/name")
    public ResponseEntity<BookDTO> findByName(@RequestBody String name) {
        return ResponseEntity.ok(bookService.findByName(name));
    }

    @GetMapping("/genres")
    public ResponseEntity<List<BookDTO>> findByAllGenres(@RequestBody List<BookGenre> genres) {
        return ResponseEntity.ok(bookService.findByGenre(genres));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}/borrowed-books")
    public ResponseEntity<List<BookDTO>> getBorrowedBooks(@PathVariable String username) {
        return ResponseEntity.ok(bookService.getBorrowedBooks(username));
    }

    @PatchMapping("/{username}/borrow-book/{id}")
    public ResponseEntity<List<BookDTO>> borrowBook(@PathVariable String username, @PathVariable Long id) throws Exception{
        return ResponseEntity.ok(bookService.borrowBookByUser(username, id));
    }

    @PatchMapping("/{username}/return-book/{id}")
    public ResponseEntity<List<BookDTO>> returnBook(@PathVariable String username, @PathVariable Long id) throws Exception {
        return ResponseEntity.ok(bookService.returnBookByUser(username, id));
    }

    @PatchMapping("/{username}/return-all-books")
    public ResponseEntity<Void> returnAllBooksByUser(@PathVariable String username) {
        bookService.returnAllBorrowedBooksByUser(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}/borrowing-history")
    public ResponseEntity<Map<String, Date>> getBorrowingHistoryForUser(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(bookService.getBorrowingHistoryForUser(username));
    }

    @GetMapping("/lost-books")
    public ResponseEntity<List<BookDTO>> getLostBooks() {
        return ResponseEntity.ok(bookService.getLostBooks());
    }


}
