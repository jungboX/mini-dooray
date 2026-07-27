package com.minidooray.task.repository;

import com.minidooray.task.model.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

    List<Milestone> findByProjectId(Long projectId); // 프로젝트의 모든 마일스톤 조회

}
