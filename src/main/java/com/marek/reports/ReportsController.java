package com.marek.reports;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/reports")
public class ReportsController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/users-and-borrow-count")
    public ResponseEntity<HashMap<String, Integer>> getUsersAndBorrowCount() {
        return ResponseEntity.ok(reportService.getUsersAndBorrowedBookCount());
    }

    @GetMapping("/average-borrow-count-per-year")
    public ResponseEntity<Float> getAverageBorrowCountPerYear() {
        return ResponseEntity.ok(reportService.getYearlyAverageBorrowings());
    }

    @GetMapping("/top-{count}-books")
    public ResponseEntity<List<String>> getTopBooks(@PathVariable int count) {
        return ResponseEntity.ok(reportService.getTopBorrowedBooks(count));
    }
}
