package com.minidooray.task.service;

import com.minidooray.task.dto.comment.CommentCreateRequest;
import com.minidooray.task.dto.comment.CommentResponse;
import com.minidooray.task.model.Comment;
import com.minidooray.task.model.Project;
import com.minidooray.task.model.Task;
import com.minidooray.task.repository.CommentRepository;
import com.minidooray.task.repository.ProjectMemberRepository;
import com.minidooray.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;

    // 댓글 생성
    @Transactional
    public CommentResponse createComment(Long taskId, CommentCreateRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task ID " + taskId + "를 찾을 수 없습니다."));

        Project project = task.getProject();
        boolean isMember = projectMemberRepository
                .findByProjectIdAndUsername(project.getId(), request.username()).
                isPresent();

        if (!isMember) {
            throw new SecurityException("프로젝트 멤버만 댓글을 작성할 수 있습니다.");
        }

        Comment comment = new Comment(
                0L,
                task,
                request.username(),
                request.content()
        );

        Comment savedComment = commentRepository.save(comment);

        return convertToResponse(savedComment);
    }

    // 댓글 조회 (단일)
    @Transactional(readOnly = true)
    public CommentResponse getComment(Long taskId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment ID " + commentId + "를 찾을 수 없습니다."));

        if (comment.getTask().getId() != taskId) {
            throw new IllegalArgumentException("Comment ID " + commentId + "는 Task ID " + taskId + "에 속하지 않습니다.");
        }

        return convertToResponse(comment);
    }

    // 댓글 전체 조회 (목록)
    @Transactional(readOnly = true)
    public List<CommentResponse> getAllCommentsByTaskId(Long taskId) {
        List<Comment> comments = commentRepository.findAllByTaskId(taskId);

        return comments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 댓글 수정 (content만 수정 가능하도록 가정)
    @Transactional
    public CommentResponse updateComment(Long taskId, Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment ID " + commentId + "를 찾을 수 없습니다."));

        if (comment.getTask().getId() != taskId) { //
            throw new IllegalArgumentException("Comment ID " + commentId + "는 Task ID " + taskId + "에 속하지 않습니다.");
        }

        if (newContent != null) {
            comment.setContent(newContent);
        }

        Comment updatedComment = commentRepository.save(comment);
        return convertToResponse(updatedComment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long taskId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment ID " + commentId + "를 찾을 수 없습니다."));

        if (comment.getTask().getId() != taskId) { //
            throw new IllegalArgumentException("Comment ID " + commentId + "는 Task ID " + taskId + "에 속하지 않습니다.");
        }
        commentRepository.delete(comment);
    }

    private CommentResponse convertToResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getTask().getId(),
                comment.getUsername(),
                comment.getContent()
        );
    }
}