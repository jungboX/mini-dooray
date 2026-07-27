package com.nhnacademy.springminidooray.controller;

import com.nhnacademy.springminidooray.model.ProjectCreateRequest;
import com.nhnacademy.springminidooray.model.Status;
import com.nhnacademy.springminidooray.model.dto.ProjectDetailResponse;
import com.nhnacademy.springminidooray.model.dto.ProjectResponse;
import com.nhnacademy.springminidooray.service.TaskApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final RedisTemplate redisTemplate;

    private final TaskApiService taskApiService;

    @GetMapping
    public String getProjects(@CookieValue("SESSIONID") String sessionId,
                             Model model) {
        String username = (String) redisTemplate.opsForValue().get(sessionId);

        List<ProjectResponse> projects = taskApiService.getProjectList(username);

        model.addAttribute("username", username);
        model.addAttribute("projects", projects);

        return "projects";
    }

    @PostMapping
    public String addProject(@RequestParam String title,
                             @RequestParam Status status,
                             @CookieValue("SESSIONID") String sessionId
                             ) {
        String username = (String) redisTemplate.opsForValue().get(sessionId);

        ProjectCreateRequest request = new ProjectCreateRequest(username, title, status);
        taskApiService.createProject(request);

        return "redirect:/projects";

    }

    @GetMapping("/{id}")
    public String getProject(@PathVariable int id,
                             Model model) {
        ProjectDetailResponse projectDetailResponse =  taskApiService.getProjectDetails(id);

        model.addAttribute("projectDetails", projectDetailResponse);
        model.addAttribute("members", projectDetailResponse.members());
        model.addAttribute("tasks", projectDetailResponse.tasks());
        model.addAttribute("milestones", projectDetailResponse.milestones());
        model.addAttribute("tags", projectDetailResponse.tags());

        return "projectDetail";
    }


    @GetMapping("/form")
    public String getProjectForm(Model model) {
        ProjectCreateRequest request = new ProjectCreateRequest(null, null, null);
        model.addAttribute("projectRequest", request);

        return "projectsForm";
    }




}
