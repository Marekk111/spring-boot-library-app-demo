package com.marek.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BorrowingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;
    private Long bookId;
    @Temporal(TemporalType.DATE)
    private Date borrowingDate = new Date();

    @Temporal(TemporalType.DATE)
    private Date returnDate;
    private boolean currentlyBorrowed;
}