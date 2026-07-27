package com.minidooray.task.service;

import com.minidooray.task.dto.milestone.MilestoneCreateRequest;
import com.minidooray.task.dto.milestone.MilestoneResponse;
import com.minidooray.task.dto.milestone.MilestoneUpdateRequest;
import com.minidooray.task.model.Milestone;
import com.minidooray.task.model.Project;
import com.minidooray.task.repository.MilestoneRepository;
import com.minidooray.task.repository.ProjectRepository;
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
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository; // TaskRepository 주입 필요

    @Transactional
    public MilestoneResponse createMilestone(Long projectId, MilestoneCreateRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("프로젝트 ID " + projectId + "를 찾을 수 없습니다."));

        Milestone milestone = new Milestone(
                0L, // ID는 자동 생성되므로 0 또는 null
                request.getContent(),
                request.getStartedAt(),
                request.getEndedAt(),
                project
        );
        Milestone savedMilestone = milestoneRepository.save(milestone);

        return new MilestoneResponse(
                savedMilestone.getId(),
                savedMilestone.getContent(),
                savedMilestone.getStartedAt(),
                savedMilestone.getEndedAt(),
                projectId
        );
    }

    @Transactional
    public void deleteMilestone(Long projectId, Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new NoSuchElementException("마일스톤 ID " + milestoneId + "를 찾을 수 없습니다."));

        if (milestone.getProject().getId() != projectId) {            // 소유권이 없거나, 마일스톤이 해당 프로젝트에 속하지 않으면 접근 거부
            throw new IllegalArgumentException("마일스톤 ID " + milestoneId + "는 프로젝트 ID " + projectId + "에 속하지 않습니다.");
        }

        // 1. 외래 키 제약 조건 해결: 해당 마일스톤에 연결된 Task들의 마일스톤 ID를 NULL로 설정합니다.
        taskRepository.disassociateMilestoneFromTasks(milestoneId);


        milestoneRepository.delete(milestone);
    }

    @Transactional(readOnly = true)
    public MilestoneResponse getMilestone(Long projectId, Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new NoSuchElementException("마일스톤 ID " + milestoneId + "를 찾을 수 없습니다."));

        if (milestone.getProject().getId() != projectId) {
            throw new IllegalArgumentException("마일스톤 ID " + milestoneId + "는 프로젝트 ID " + projectId + "에 속하지 않습니다.");
        }

        return new MilestoneResponse(
                milestone.getId(),
                milestone.getContent(),
                milestone.getStartedAt(),
                milestone.getEndedAt(),
                projectId
        );
    }
    // 프로젝트의 마일스톤 전체 조회
    @Transactional(readOnly = true)
    public List<MilestoneResponse> getMilestones(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException(
                        "프로젝트 ID " + projectId + "를 찾을 수 없습니다."));

        List<Milestone> milestones = milestoneRepository.findByProjectId(projectId);

        return milestones.stream()
                .map(milestone -> new MilestoneResponse(
                        milestone.getId(),
                        milestone.getContent(),
                        milestone.getStartedAt(),
                        milestone.getEndedAt(),
                        projectId
                ))
                .collect(Collectors.toList());
    }
    @Transactional
    public MilestoneResponse updateMilestone(Long projectId, Long milestoneId, MilestoneUpdateRequest request) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new NoSuchElementException("Milestone ID " + milestoneId + "를 찾을 수 없습니다."));

        if (milestone.getProject().getId() != projectId) {
            throw new IllegalArgumentException("마일스톤 ID " + milestoneId + "는 프로젝트 ID " + projectId + "에 속하지 않습니다.");
        }

        milestone.updateDetails(
                request.content(),
                request.startedAt(),
                request.endedAt()
        );

        Milestone updatedMilestone = milestoneRepository.save(milestone);

        return new MilestoneResponse(
                updatedMilestone.getId(),
                updatedMilestone.getContent(),
                updatedMilestone.getStartedAt(),
                updatedMilestone.getEndedAt(),
                projectId
        );
    }
}