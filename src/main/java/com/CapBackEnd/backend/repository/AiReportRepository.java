package com.CapBackEnd.backend.repository;

import com.CapBackEnd.backend.entity.AiReport;
import com.CapBackEnd.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiReportRepository extends JpaRepository<AiReport, Long> {
    // 해당 유저의 분석 기록 다 가져오기
    List<AiReport> findByUserOrderByAnalyzedAtDesc(User user);
}
