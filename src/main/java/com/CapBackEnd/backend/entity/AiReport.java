package com.CapBackEnd.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ai_reports")
public class AiReport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String reportMonth;
    private Integer currentTotal;
    private String status;
    private Double avgUsage;

    @Column(columnDefinition = "TEXT")
    private String summary;

    // 분석된 구독 리스트 + 해지 후보 리스트 JSON에 저장
    @Column(columnDefinition = "TEXT")
    private String analysisResultJson;

    @CreationTimestamp
    private LocalDateTime analyzedAt;

    @Builder
    public AiReport(User user, String reportMonth, Integer currentTotal, String status,
                    Double avgUsage, String summary, String analysisResultJson) {
        this.user = user;
        this.reportMonth = reportMonth;
        this.currentTotal = currentTotal;
        this.status = status;
        this.avgUsage = avgUsage;
        this.summary = summary;
        this.analysisResultJson = analysisResultJson;
    }
}
