package com.marek.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingHistoryRepository extends JpaRepository<BorrowingHistory, Long> {

    List<BorrowingHistory> findByUserId(Long userId);
    List<BorrowingHistory> findByBookId(Long bookId);
    List<BorrowingHistory> findByReturnDateIsNull();

}
