package com.feijian.dao.projectDetail;

import com.feijian.domain.FileDescription;
import com.feijian.domain.Project;
import com.feijian.domain.projectDetail.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDescriptionRepository extends JpaRepository<FileDescription,Integer>, JpaSpecificationExecutor<FileDescriptionRepository> {
    public List<FileDescription> getFileDescriptionsByFileNameLike(String fileName);
    public List<FileDescription> getFileDescriptionsByFileTypeLike(String fileType);
    public List<FileDescription> getFileDescriptionsByDetailLike(String key);
    public Page<FileDescription> getFileDescriptionsByProject(Project project, Pageable pageable);
    public Page<FileDescription> getFileDescriptionsByStatus(Status status, Pageable pageable);
}
