package com.minidooray.task.controller;

import com.minidooray.task.dto.milestone.MilestoneCreateRequest;
import com.minidooray.task.dto.milestone.MilestoneResponse;
import com.minidooray.task.dto.milestone.MilestoneUpdateRequest;
import com.minidooray.task.service.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/projects")
public class MilestoneController {

    private final MilestoneService milestoneService;

    @Autowired
    public MilestoneController(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }

    // 마일스톤 생성
    @PostMapping("/{projectId}/milestones")
    public ResponseEntity<MilestoneResponse> createMilestone(@PathVariable Long projectId,
                                                             @RequestBody MilestoneCreateRequest request){
        MilestoneResponse response = milestoneService.createMilestone(projectId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 마일스톤 조회
    @GetMapping("/{projectId}/milestones/{milestoneId}")
    public ResponseEntity<MilestoneResponse> getMilestone(
            @PathVariable Long projectId,
            @PathVariable Long milestoneId) {

        MilestoneResponse response = milestoneService.getMilestone(projectId, milestoneId);

        return ResponseEntity.ok(response); // 200 OK
    }
    // 마일스톤 리스트
    @GetMapping("/{projectId}/milestones")
    public ResponseEntity<List<MilestoneResponse>> getMilestones(
            @PathVariable Long projectId) {

        List<MilestoneResponse> milestones = milestoneService.getMilestones(projectId);

        return ResponseEntity.ok(milestones); // 200 OK
    }

    // 마일스톤 수정
    @PutMapping("/{projectId}/milestones/{milestoneId}")
    public ResponseEntity<MilestoneResponse> updateMilestone(
            @PathVariable Long projectId,
            @PathVariable Long milestoneId,
            @RequestBody MilestoneUpdateRequest request) { // MilestoneUpdateRequest 사용

        MilestoneResponse response = milestoneService.updateMilestone(projectId, milestoneId, request);

        return ResponseEntity.ok(response);
    }

    // 마일스톤 삭제
    @DeleteMapping("/{projectId}/milestones/{milestoneId}")
    public ResponseEntity<Void> deleteMilestone(@PathVariable Long projectId, @PathVariable Long milestoneId){
        milestoneService.deleteMilestone(projectId,milestoneId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
