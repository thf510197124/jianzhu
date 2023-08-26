package com.feijian.service.impl;

import com.feijian.dao.ProjectItemRepository;
import com.feijian.dao.ProjectRepository;
import com.feijian.domain.Project;
import com.feijian.domain.ProjectItem;
import com.feijian.domain.projectDetail.ProjectDetail;
import com.feijian.item.ProjectStatus;
import com.feijian.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    ProjectRepository proRepository;
    ProjectItemRepository pimRepository;
    @Autowired
    public ProjectServiceImpl(ProjectRepository proRepository, ProjectItemRepository pimRepository) {
        this.proRepository = proRepository;
        this.pimRepository = pimRepository;
    }

    @Override
    public Project createProject(Project project) {
        proRepository.save(project);
        return project;
    }

    @Override
    public Project updateProject(Project project) {
        proRepository.save(project);
        return project;
    }

    @Override
    public boolean deleteProject(Project project) {
        if (project.getProjectItems() == null || project.getProjectItems().size() == 0){
            proRepository.delete(project);
            return true;
        }
        return false;
    }

    @Override
    public ProjectItem createProjectItem(ProjectItem projectItem, Project project) {
        projectItem.setProject(project);
        pimRepository.save(projectItem);
        return projectItem;
    }

    @Override
    public ProjectItem updateProjectItem(ProjectItem projectItem) {
        pimRepository.save(projectItem);
        return projectItem;
    }

    @Override
    public boolean deleteProjectItem(ProjectItem projectItem) {
        pimRepository.delete(projectItem);
        return true;
    }

    @Override
    public Page<Project> onProcess(Pageable pageable) {
        return proRepository.findProjectByStatusOrderByIdDesc(ProjectStatus.施工中,pageable);
    }

    @Override
    public Project get(int proJd) {
        return proRepository.getOne(proJd);
    }

    @Override
    public ProjectItem getItem(int proItemId) {
        return pimRepository.getOne(proItemId);
    }

    @Override
    public List<Project> findProjectByBidTimeBetween(Date start, Date end) {
        return proRepository.findProjectByBidTimeBetween(start,end);
    }

    @Override
    public List<Project> findProjectByWorkTime(Date start, Date end) {
        return proRepository.findProjectByWorkTime(start,end);
    }


    @Override
    public Page<Project> findAll(Pageable pageable) {
        return proRepository.findAll(pageable);
    }

    @Override
    public Project getProjectByName(String name) {
        return proRepository.findByProjectName(name);
    }

    @Override
    public List<Project> getByProjectNameLike(String key) {
        return proRepository.findByProjectNameLike(key);
    }

    @Override
    public ProjectItem getByItemName(Project project, String name) {
        List<ProjectItem> items = project.getProjectItems();
        for (ProjectItem item : items){
            if (item.getItemName().equals(name)){
                return item;
            }
        }
        return null;
    }

    @Override
    public Project getByProjectDetail(ProjectDetail projectDetail) {
        return proRepository.getProjectByProjectDetail(projectDetail);
    }

    @Override
    public void updateProjectMaterialBegin(int projectId, int startIndex) {
        proRepository.updateProjectMaterialBegin(projectId,startIndex);
    }

    @Override
    public void updateProjectMaterialEnd(int projectId, int endIndex) {
        proRepository.updateProjectMaterialEnd(projectId,endIndex);
    }

    @Override
    public void updateProjectItemMaterialBegin(int projectItemId, int startIndex) {
        pimRepository.updateProjectItemMaterialBegin(projectItemId,startIndex);
    }

    @Override
    public void updateProjectItemMaterialEnd(int projectItemId, int endIndex) {
        pimRepository.updateProjectItemMaterialEnd(projectItemId,endIndex);
    }

    @Override
    public List<String> allProjectNames() {
        return proRepository.findAll().stream().map(Project::getProjectName).collect(Collectors.toList());
    }
}
