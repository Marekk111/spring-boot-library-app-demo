package com.marek.book;

import com.marek.Authentication.UserEntity;
import com.marek.shared.BookGenre;
import jakarta.persistence.*;
import lombok.*;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String author;
    private String name;
    private String description;

    @ElementCollection(targetClass = BookGenre.class)
    @Enumerated(EnumType.STRING)
    private List<BookGenre> genres = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity borrower;

    private boolean isCurrentlyBorrowed = false;
    //@ElementCollection
    //@Temporal(TemporalType.DATE)
    //private List<Date> borrowedOnDates = new ArrayList<>();
    private boolean isLost;

//    public void checkIfLost() {
//        if (isCurrentlyBorrowed && borrowedOnDates != null && !borrowedOnDates.isEmpty()) {
//            Date lastBorrowedDate = borrowedOnDates.getLast();
//            long diffMillis = new Date().getTime() - lastBorrowedDate.getTime();
//            long diffDays = diffMillis / (1000 * 60 * 60 * 24);
//            if (diffDays > 30) {
//                this.isLost = true;
//            }
//        }
//    }
}
