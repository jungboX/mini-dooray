package com.minidooray.task.repository;

import com.minidooray.task.dto.tag.TagResponse;
import com.minidooray.task.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<TagResponse> findALlByProjectId(long projectId);
}
