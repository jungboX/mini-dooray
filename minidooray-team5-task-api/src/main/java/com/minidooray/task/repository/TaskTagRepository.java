package com.minidooray.task.repository;

import com.minidooray.task.model.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {
    // Task의 모든 TaskTag 조회
    List<TaskTag> findByTaskId(Long taskId);

    // Tag의 모든 TaskTag 조회
    List<TaskTag> findByTagId(Long tagId);

    // Task에 특정 Tag가 이미 연결되어 있는지 확인
    boolean existsByTaskIdAndTagId(Long taskId, Long tagId);


    // ⭐ Task ID와 Tag ID를 모두 사용하여 TaskTag 레코드를 조회
    // TaskTag 엔티티의 task(Task 객체)의 id, tag(Tag 객체)의 id를 참조합니다.
    Optional<TaskTag> findByTask_IdAndTag_Id(Long taskId, Long tagId);
    void deleteAllByTaskId(long taskId);
}
