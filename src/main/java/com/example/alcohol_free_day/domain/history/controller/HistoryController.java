package com.example.alcohol_free_day.domain.history.controller;

import com.example.alcohol_free_day.domain.history.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
@CrossOrigin("*")
public class HistoryController {

    private final HistoryService historyService;
}
