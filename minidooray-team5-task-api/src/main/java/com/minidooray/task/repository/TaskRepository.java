package com.minidooray.task.repository;

import com.minidooray.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findALlByProjectId(long projectId);

    // 특정 마일스톤 ID를 가진 모든 Task의 Milestone_id를 NULL로 업데이트
    @Modifying
    @Query("UPDATE Task t SET t.milestone = NULL WHERE t.milestone.id = :milestoneId")
    void disassociateMilestoneFromTasks(@Param("milestoneId") Long milestoneId);
}
