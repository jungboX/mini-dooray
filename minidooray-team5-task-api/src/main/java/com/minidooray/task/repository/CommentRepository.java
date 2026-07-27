package com.minidooray.task.repository;

import com.minidooray.task.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTaskId(long taskId); // 댓글 전체 조회

    void deleteAllByTaskId(Long taskId); // task 관련 댓글 모두 삭제
}
