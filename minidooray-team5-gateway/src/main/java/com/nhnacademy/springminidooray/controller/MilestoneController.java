package com.nhnacademy.springminidooray.controller;

import com.nhnacademy.springminidooray.model.dto.MilestoneCreateRequest;
import com.nhnacademy.springminidooray.service.TaskApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class MilestoneController {
    @Value("${api.task.url}")
    private String TASK_API_URL;

    private final TaskApiService taskApiService;

    @GetMapping("/projects/{projectId}/milestones")
    public String milestoneForm(@PathVariable int projectId) {
        return "milestoneForm";
    }

    @PostMapping("/projects/{projectId}/milestones")
    public String createMilestone(@PathVariable int projectId,
                                  @ModelAttribute MilestoneCreateRequest request) {
        taskApiService.createMilestone(request);

        return "redirect:/projects/" + projectId;
    }

    @DeleteMapping("/projects/{projectId}/milestones/{milestoneId}")
    public String createMilestone(@PathVariable int projectId,
                                  @PathVariable int milestoneId) {
        taskApiService.deleteMilestone(projectId, milestoneId);

        return "redirect:/projects/" + projectId;
    }
}
