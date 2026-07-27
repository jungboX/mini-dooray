package com.nhnacademy.springminidooray.controller;

import com.nhnacademy.springminidooray.model.dto.CommentCreateRequest;
import com.nhnacademy.springminidooray.model.dto.CommentUpdateRequest;
import com.nhnacademy.springminidooray.service.TaskApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class CommentController {
    private final TaskApiService taskApiService;

    //comment 작성이 taskMain에 포함되어있어서 따로 작성폼이 없음
//    @GetMapping("/projects/{projectId}/tasks/{taskId}/comments")
//    public String commentForm(@PathVariable int projectId,
//                              @PathVariable int taskId) {
//        return "commentForm";
//    }

    @PostMapping("/projects/{projectId}/tasks/{taskId}/comments")
    public String createComment(@PathVariable int projectId,
                                @PathVariable int taskId,
                                @ModelAttribute CommentCreateRequest request) {
        taskApiService.createComment(projectId, taskId, request);

        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

    @PutMapping("/projects/{projectId}/tasks/{taskId}/comments/{commentId}")
    public String updateComment(@PathVariable int projectId,
                                @PathVariable int taskId,
                                @PathVariable int commentId,
                                @RequestParam String content) {
        taskApiService.updateComment(projectId, taskId, commentId, content);

        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

    @DeleteMapping("/projects/{projectId}/tasks/{taskId}/comments/{commentId}")
    public String deleteComment(@PathVariable int projectId,
                                @PathVariable int taskId,
                                @PathVariable int commentId) {

        taskApiService.deleteComment(projectId, taskId, commentId);

        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }
}
