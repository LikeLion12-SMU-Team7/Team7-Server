package com.example.alcohol_free_day.domain.memory.entity;

import com.example.alcohol_free_day.domain.memory.dto.MemoryRequest;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memoryId;

    private LocalDate createdAt;
    private LocalDate modifiedAt;
    private String doneWhen;
    private String doneWhere;
    private String withWho;
    private String what;
    private String how;
    private String why;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void update(MemoryRequest request) {
        this.modifiedAt = LocalDate.now();
        this.doneWhen = request.when();
        this.doneWhere = request.where();
        this.withWho = request.withWho();
        this.what = request.what();
        this.how = request.how();
        this.why = request.why();
        this.content = request.content();
    }
}
