package com.feijian.service;

import com.feijian.domain.projectDetail.ProjectDetail;
import com.feijian.domain.projectDetail.Status;

public interface ProjectDetailService {
    public ProjectDetail saveProjectDetail(ProjectDetail projectDetail);
    public ProjectDetail get(int projectDetailId);
    public ProjectDetail getProjectDetailByStatus(Status status);

}
