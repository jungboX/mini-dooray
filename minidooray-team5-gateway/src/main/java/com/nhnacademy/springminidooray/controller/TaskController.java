package com.nhnacademy.springminidooray.controller;

import com.nhnacademy.springminidooray.model.dto.*;
import com.nhnacademy.springminidooray.service.TaskApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class TaskController {
    private final TaskApiService taskApiService;
    private final RedisTemplate redisTemplate;

    @GetMapping("/projects/{projectId}/tasks")
    public String getTaskForm(@PathVariable int projectId,
                              @CookieValue("SESSIONID") String sessionId,
                              Model model) {

        String username = (String) redisTemplate.opsForValue().get(sessionId);
        List<MilestoneResponse> milestones = taskApiService.getMilestoneList(projectId);

        List<TagResponse> tagList = taskApiService.getTagList(projectId);
        model.addAttribute("tags", tagList);

        model.addAttribute("milestones", milestones);
        model.addAttribute("taskRequest", new TaskCreateRequest(null, null, username, projectId, null));
        model.addAttribute("username", username);

        return "taskForm";
    }

    @PostMapping("/projects/{projectId}/tasks")
    public String createTask(@PathVariable int projectId,
                             @ModelAttribute TaskCreateRequest request) {
        taskApiService.createTask(request);

        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/projects/{projectId}/tasks/{taskId}")
    public String getTaskDetail(@PathVariable int projectId,
                              @PathVariable int taskId,
                              @CookieValue("SESSIONID") String sessionId,
                              Model model) {

        String username = (String) redisTemplate.opsForValue().get(sessionId);

        TaskDetailResponse taskDetail = taskApiService.getTaskDetails(projectId, taskId);
        List<TagResponse> tagList = taskApiService.getTagListByTaskId(projectId, taskId);

        model.addAttribute("tags", tagList);
        model.addAttribute("username", username);
        model.addAttribute("task", taskDetail);
        model.addAttribute("milestone", taskDetail.milestone());
        model.addAttribute("comments", taskDetail.commentList());
        model.addAttribute("commentRequest", new CommentCreateRequest(username, null));


        return "taskDetail";
    }

    @GetMapping("/projects/{projectId}/tasks/{taskId}/edit")
    public String taskUpdateForm(@PathVariable int projectId,
                                @PathVariable int taskId,
                                @CookieValue("SESSIONID") String sessionId,
                                Model model) {

        String username = (String) redisTemplate.opsForValue().get(sessionId);

        TaskDetailResponse taskDetail = taskApiService.getTaskDetails(projectId, taskId);

        List<MilestoneResponse> milestones = taskApiService.getMilestoneList(projectId);
        List<TagResponse> tagList = taskApiService.getTagList(projectId);
        model.addAttribute("tags", tagList);

        model.addAttribute("milestones", milestones);

        model.addAttribute("username", username);
        model.addAttribute("task", taskDetail);
        model.addAttribute("milestone", taskDetail.milestone());
        model.addAttribute("comments", taskDetail.commentList());
        model.addAttribute("commentRequest", new CommentCreateRequest(username, null));

        return "taskDetailUpdateForm";
    }

    @DeleteMapping("/projects/{projectId}/tasks/{taskId}")
    public String deleteTask(@PathVariable int projectId, @PathVariable int taskId) {
        taskApiService.deleteTask(projectId, taskId);

        return "redirect:/projects/" + projectId;
    }

    @PutMapping("/projects/{projectId}/tasks/{taskId}")
    public String updateTask(@PathVariable int projectId, @PathVariable int taskId,
                             @ModelAttribute TaskUpdateRequest request) {
        taskApiService.updateTask(projectId, taskId, request);

        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }
}
