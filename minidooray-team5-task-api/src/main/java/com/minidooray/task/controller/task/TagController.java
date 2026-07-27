package com.minidooray.task.controller.task;

import com.minidooray.task.dto.tag.TagCreateRequest;
import com.minidooray.task.dto.tag.TagResponse;
import com.minidooray.task.dto.tag.TagUpdateRequest;
import com.minidooray.task.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/projects")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }


    @GetMapping("/{projectId}/tags")
    public ResponseEntity<List<TagResponse>> getTags(@PathVariable Long projectId){

        List<TagResponse> response = tagService.readTagList(projectId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // tag 생성
    @PostMapping("/{projectId}/tags")
    public ResponseEntity<TagResponse> createTag(
            @PathVariable Long projectId, @RequestBody TagCreateRequest request){

        TagResponse response = tagService.create(projectId,request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // tag 읽기
    @GetMapping("/{projectId}/tags/{tagId}")
    public ResponseEntity<TagResponse> getTag(
            @PathVariable Long projectId, @PathVariable Long tagId){

        TagResponse response = tagService.readProjectId(projectId,tagId);

        return ResponseEntity.ok(response);
    }

    // tag 수정
    @PutMapping("/{projectId}/tags/{tagId}")
    public ResponseEntity<TagResponse> patchTag(@PathVariable Long tagId,
                                                @RequestBody TagUpdateRequest request){
        TagResponse response = tagService.updateTag(tagId, request);

        return ResponseEntity.ok(response);
    }

    // tag 삭제
    @DeleteMapping("/{projectId}/tags/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId, @PathVariable Long projectId){
        tagService.deleteTag(tagId,projectId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
