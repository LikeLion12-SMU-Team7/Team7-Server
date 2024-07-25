package com.example.alcohol_free_day.domain.history.controller;

import com.example.alcohol_free_day.domain.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
public class HistoryController {

    private final HistoryService historyService;
}
