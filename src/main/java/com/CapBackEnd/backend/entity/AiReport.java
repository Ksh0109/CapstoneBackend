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

    @Column(name = "report_month")
    private String reportMonth;

    private Integer currentTotal;
    private Integer score;
    private String status;

    private Integer predictedTotal;
    private String predictionComment;

    private String summary;

    @Column(columnDefinition = "TEXT")
    private String advice;

    // 그래프 데이터
    @Column(name = "category_json", columnDefinition = "TEXT")
    private String categoryJson;

    @CreationTimestamp
    private LocalDateTime analyzedAt;

    @Builder
    public AiReport(User user, String reportMonth, Integer currentTotal, Integer score, String status,
                    Integer predictedTotal, String predictionComment, String summary, String advice, String categoryJson) {
        this.user = user;
        this.reportMonth = reportMonth;
        this.currentTotal = currentTotal;
        this.score = score;
        this.status = status;
        this.predictedTotal = predictedTotal;
        this.predictionComment = predictionComment;
        this.summary = summary;
        this.advice = advice;
        this.categoryJson = categoryJson;
    }
}
