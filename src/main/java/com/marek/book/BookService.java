package com.marek.book;

import com.marek.Authentication.UserEntity;
import com.marek.Authentication.UserRepository;
import com.marek.shared.BookGenre;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private final BookMapper bookMapper;
    private final BorrowingHistoryRepository borrowingHistoryRepo;

    public BookDTO createBook(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        book = bookRepo.save(book);
        return bookMapper.toDTO(book);
    }

    public List<BookDTO> createManyBooks(List<BookDTO> bookDTOList) {
        List<Book> books = bookMapper.toEntity(bookDTOList);
        books = bookRepo.saveAll(books);
        return bookMapper.toDTO(books);
    }

    public BookDTO updateBook(Long bookId, BookDTO bookDTO) throws Exception {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found with id " + bookId));
        book.setAuthor(bookDTO.getAuthor());
        book.setName(bookDTO.getName());
        book.setDescription(bookDTO.getDescription());
        book.setGenres(bookDTO.getGenres());
        bookRepo.save(book);
        return bookMapper.toDTO(book);
    }

    public void deleteBook(Long bookId) {
        bookRepo.deleteById(bookId);
    }

    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepo.findAll();
        return bookMapper.toDTO(books);
    }

    public BookDTO getBookById(Long bookId) {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
        return bookMapper.toDTO(book);
    }

    public List<BookDTO> findByAuthor(String author) {
        List<Book> books = bookRepo.findByAuthor(author);
        return bookMapper.toDTO(books);
    }

    public BookDTO findByName(String name) {
        Book book = bookRepo.findByName(name).orElseThrow(() -> new EntityNotFoundException("Book not found with name: " + name));
        return bookMapper.toDTO(book);
    }

    public List<BookDTO> findByGenre(List<BookGenre> bookGenres) { //returns all books that match all the specified genres
        List<Book> books = bookRepo.findByAllGenres(bookGenres, bookGenres.size());
        return bookMapper.toDTO(books);
    }

    public List<BookDTO> getBorrowedBooks(String username) {
        UserEntity user = userRepo.findByUsername(username);
        return bookMapper.toDTO(user.getBorrowedBooks());
    }

    public Map<String, Date> getBorrowingHistoryForUser(String username) throws Exception {
        UserEntity user = userRepo.findByUsername(username);
        List<BorrowingHistory> borrowingHistory = borrowingHistoryRepo.findByUserId(user.getId());
        Map<String, Date> borrowHistory = new HashMap<>();
        for (BorrowingHistory borrowInstance : borrowingHistory) {
            Book borrowedBook = bookRepo.findById(borrowInstance.getBookId()).orElseThrow(() -> new Exception("Book does not exist!"));
            borrowHistory.put(borrowedBook.getName(), borrowInstance.getBorrowingDate());
        }
        return borrowHistory;
    }

    public List<BookDTO> borrowBookByUser(String username, Long bookId) throws Exception{
        UserEntity user = userRepo.findByUsername(username);
        List<Book> borrowedBooks = user.getBorrowedBooks();
        Book newBook = bookRepo.findById(bookId).orElseThrow(() -> new Exception("Book to borrow not found."));
        if (newBook.isCurrentlyBorrowed()) {
            throw new IllegalStateException("Book is already borrowed!");
        }
        newBook.setBorrower(user);
        borrowedBooks.add(newBook);
        newBook.setCurrentlyBorrowed(true);
        BorrowingHistory borrowingHistory = new BorrowingHistory(null,user.getId(), bookId, new Date(), null, true);
        borrowingHistoryRepo.save(borrowingHistory);
        userRepo.save(user);
        return bookMapper.toDTO(borrowedBooks);
    }

    public void returnAllBorrowedBooksByUser(String username) {
        UserEntity user = userRepo.findByUsername(username);
        List<Book> borrowedBooks = user.getBorrowedBooks();
        for (Book book : borrowedBooks) {
            book.setCurrentlyBorrowed(false);
            book.setLost(false);
            book.setBorrower(null);
        }
        borrowedBooks.clear();
        userRepo.save(user);
    }

    public List<BookDTO> returnBookByUser(String username, Long bookId) throws Exception{
        UserEntity user = userRepo.findByUsername(username);
        List<Book> borrowedBooks = user.getBorrowedBooks();
        Book bookToReturn = bookRepo.findById(bookId).orElseThrow(() -> new Exception("Book to return not found."));
        if (!bookToReturn.isCurrentlyBorrowed()) {
            throw new IllegalStateException("Book is already returned!");
        }
        borrowedBooks.remove(bookToReturn);
        bookToReturn.setBorrower(null);
        bookToReturn.setCurrentlyBorrowed(false);
        bookToReturn.setLost(false);
        userRepo.save(user);
        return bookMapper.toDTO(borrowedBooks);
    }

    @Scheduled(cron = "0 0 * * *")
    @Transactional
    public void checkLostBooksDaily() {

        List<BorrowingHistory> activeBorrowings = borrowingHistoryRepo.findByReturnDateIsNull();

        for (BorrowingHistory borrowing : activeBorrowings) {
            Date borrowDate = borrowing.getBorrowingDate();

            long diffInMs = Math.abs(new Date().getTime() - borrowDate.getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS);

            if (diffInDays > 30) {
                Optional<Book> bookOpt = bookRepo.findById(borrowing.getBookId());
                if (bookOpt.isPresent()) {
                    Book book = bookOpt.get();
                    book.setLost(true);
                    bookRepo.save(book);
                }
            }
        }

    }

    public List<BookDTO> getLostBooks() {
        List<Book> lostBooks = bookRepo.findByIsLostTrue();
        return bookMapper.toDTO(lostBooks);
    }
}
