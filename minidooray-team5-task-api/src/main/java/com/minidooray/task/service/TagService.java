package com.minidooray.task.service;

import com.minidooray.task.dto.tag.TagCreateRequest;
import com.minidooray.task.dto.tag.TagResponse;
import com.minidooray.task.dto.tag.TagUpdateRequest;
import com.minidooray.task.dto.task.TaskResponse;
import com.minidooray.task.model.Project;
import com.minidooray.task.model.Tag;
import com.minidooray.task.model.TaskTag;
import com.minidooray.task.repository.ProjectRepository;
import com.minidooray.task.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;

    @Autowired // create
    public TagService(TagRepository tagRepository, ProjectRepository projectRepository) {
        this.tagRepository = tagRepository;
        this.projectRepository = projectRepository;
    }
    @Transactional
    public TagResponse create(Long projectId, TagCreateRequest request) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project ID " + projectId + " 찾을 수 없습니다."));

        Tag tag = new Tag(0L, request.content(), project);
        Tag savedTag = tagRepository.save(tag);
        return new TagResponse(savedTag.getId(),savedTag.getContent(),savedTag.getProject().getId());
    }

    @Transactional(readOnly = true) // read
    public TagResponse readProjectId(Long taskId, Long taskTagId) {
        Tag tag = tagRepository.findById(taskTagId)
                .orElseThrow(() -> new NoSuchElementException("Project ID " + taskId + " 찾을 수 없습니다."));

        if(tag.getProject().getId() != taskId){
            throw new IllegalArgumentException("Project ID " + taskTagId + "Project " + taskId + "에 속하지 않습니다.");
        }

        return convertToResponse(tag);

    }

    @Transactional(readOnly = true) // read
    public List<TagResponse> readTagList(Long projectId) {
        List<TagResponse> tags = tagRepository.findALlByProjectId(projectId);

        return tags;
    }



    public TagResponse updateTag(Long tagId, TagUpdateRequest request) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        if(request.content() != null){
            tag.setContent(request.content());
        }

        if(request.projectId() != null){
            Project project = projectRepository.findById(request.projectId())
                    .orElseThrow(() -> new NoSuchElementException("Project not found"));

            tag.setProject(project);
        }

        Tag updatedTag = tagRepository.save(tag);

        return convertToResponse(updatedTag);
    }

    public void deleteTag(Long tagId, Long projectId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NoSuchElementException("Project ID " + tagId + " 찾을 수 없습니다."));

        if(tag.getProject().getId() != projectId){
            throw new IllegalArgumentException("Tag ID " + tagId + "는 프로젝트 ID " + projectId + "에 속하지 않습니다.");
        }
        tagRepository.delete(tag);
    }


    private TagResponse convertToResponse(Tag tag) {  // response 객체로 반환 메서드
    Long projectId = (tag.getProject() != null) ? tag.getProject().getId() : null;

        return new TagResponse(
                tag.getId(),
                tag.getContent(),
                projectId
        );

    }
    // Tag가 사용된 모든 Task 조회
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Tag ID " + tagId + "를 찾을 수 없습니다."));

        // @OneToMany 활용!
        return tag.getTaskTags().stream()
                .map(TaskTag::getTask)
                .map(task -> new TaskResponse(
                        task.getTitle(),
                        task.getContent(),
                        task.getUsername(),
                        task.getMilestone() != null ? task.getMilestone().getId() : null
                ))
                .collect(Collectors.toList());
    }


}
