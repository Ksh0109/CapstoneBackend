package com.CapBackEnd.backend.repository;

import com.CapBackEnd.backend.entity.AiReport;
import com.CapBackEnd.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AiReportRepository extends JpaRepository<AiReport, Long> {
    // 해당 유저의 분석 기록 다 가져오기
    Optional<AiReport> findByUserAndReportMonth(User user,String reportMonth);

    // 계정 삭제 시 전체 리포트 정리용
    List<AiReport> findAllByUser(User user);
}
