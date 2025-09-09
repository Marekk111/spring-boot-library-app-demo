package com.marek.reports;

import com.marek.Authentication.UserEntity;
import com.marek.Authentication.UserRepository;
import com.marek.book.Book;
import com.marek.book.BookRepository;
import com.marek.book.BorrowingHistory;
import com.marek.book.BorrowingHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportService {
    private final UserRepository userRepo;
    private final BookRepository bookRepo;
    private final BorrowingHistoryRepository borrowingHistoryRepo;

    public HashMap<String, Integer> getUsersAndBorrowedBookCount() {
        List<UserEntity> users = userRepo.findAll();
        HashMap<String, Integer> usersWithBookCount = new HashMap<>();
        for (UserEntity user : users) {
            usersWithBookCount.put(user.getUsername(), user.getBorrowedBooks().size());
        }
        return usersWithBookCount;
    }

    public float getYearlyAverageBorrowings() {
        List<BorrowingHistory> allBorrowings = borrowingHistoryRepo.findAll();

        if (allBorrowings.isEmpty()) {
            return 0.0f;
        }

        Map<Integer, Long> borrowingsByYear = allBorrowings.stream()
                .collect(Collectors.groupingBy(
                        borrowing -> {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(borrowing.getBorrowingDate());
                            return cal.get(Calendar.YEAR);
                        },
                        Collectors.counting()
                ));

        long totalBorrowings = borrowingsByYear.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        int numberOfYears = borrowingsByYear.size();
        return (float) totalBorrowings / numberOfYears;
    }

    public List<String> getTopBorrowedBooks(int count) {
        if (count <= 0) {
            return Collections.emptyList();
        }

        List<BorrowingHistory> allBorrowings = borrowingHistoryRepo.findAll();

        Map<Long, Long> bookBorrowCounts = allBorrowings.stream()
                .collect(Collectors.groupingBy(
                        BorrowingHistory::getBookId,
                        Collectors.counting()
                ));

        List<Long> topBookIds = bookBorrowCounts.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(count)
                .map(Map.Entry::getKey)
                .toList();

        List<Book> topBooks = bookRepo.findAllById(topBookIds);

        Map<Long, String> bookIdToName = topBooks.stream()
                .collect(Collectors.toMap(Book::getId, Book::getName));

        return topBookIds.stream()
                .map(bookIdToName::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
