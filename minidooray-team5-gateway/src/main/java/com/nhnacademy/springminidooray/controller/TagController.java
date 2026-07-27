package com.nhnacademy.springminidooray.controller;

import com.nhnacademy.springminidooray.model.dto.TagCreateRequest;
import com.nhnacademy.springminidooray.model.dto.TagResponse;
import com.nhnacademy.springminidooray.service.TaskApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class TagController {
    private final TaskApiService taskApiService;

    @GetMapping("/projects/{projectId}/tags")
    public String tagForm(@PathVariable int projectId,
                          Model model) {
        List<TagResponse> tagList = taskApiService.getTagList(projectId);
        model.addAttribute("tags", tagList);
        return "tagForm";
    }

    @PostMapping("/projects/{projectId}/tags")
    public String createTag(@PathVariable int projectId,
                            @ModelAttribute TagCreateRequest request) {
        taskApiService.createTag(projectId, request);

        return "redirect:/projects/" + projectId;
    }

    @DeleteMapping("/projects/{projectId}/tags/{taskId}")
    public String deleteTag(@PathVariable int projectId,
                            @PathVariable int taskId) {
        taskApiService.deleteTag(projectId, taskId);

        return "redirect:/projects/" + projectId;
    }
}
