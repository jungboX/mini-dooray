package com.minidooray.task.controller;

import com.minidooray.task.dto.comment.CommentCreateRequest;
import com.minidooray.task.dto.comment.CommentResponse;
import com.minidooray.task.dto.comment.CommentUpdateRequest;
import com.minidooray.task.repository.CommentRepository;
import com.minidooray.task.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class CommentController {

    private final CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    // 댓글 생성
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long taskId,
                                                         @RequestBody CommentCreateRequest request){

        CommentResponse response = commentService.createComment(taskId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // 댓글 단일 조회
    @GetMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable Long taskId,
                                                      @PathVariable Long commentId){

        CommentResponse response = commentService.getComment(taskId, commentId);

        return ResponseEntity.ok(response);
    }

    // 댓글 목록 조회 (GET)
    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<CommentResponse>> getAllComments(@PathVariable Long taskId){

        List<CommentResponse> responseList = commentService.getAllCommentsByTaskId(taskId);

        return ResponseEntity.ok(responseList);
    }

    // 댓글 수정
    @PutMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long taskId,
                                                         @PathVariable Long commentId,
                                                         @RequestBody CommentUpdateRequest request){

        CommentResponse response = commentService.updateComment(taskId, commentId, request.content());

        return ResponseEntity.ok(response);
    }


    // 댓글 삭제
    @DeleteMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long taskId,
                                              @PathVariable Long commentId){

        commentService.deleteComment(taskId, commentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
