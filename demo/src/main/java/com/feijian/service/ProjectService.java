package com.feijian.service;

import com.feijian.domain.Company;
import com.feijian.domain.MaterialItem;
import com.feijian.domain.Project;
import com.feijian.domain.ProjectItem;
import com.feijian.domain.projectDetail.ProjectDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 工程管理
 */

public interface ProjectService {
    /**
     * 创建工程
     * @return 返回创建的工程
     */
    Project createProject(Project project);

    /**
     * 修改工程
     * @return 返回修改后的工程
     */
    Project updateProject(Project project);

    /**
     * 删除工程，可能投标等原因没有接下来
     * @return true表示删除成功，false表示删除失败
     */
    boolean deleteProject(Project project);

    /**
     * 创建分项工程
     * @param projectItem 分项工程
     * @param project 所属工程Id
     * @return 创建的分项工程
     */
    ProjectItem createProjectItem(ProjectItem projectItem,Project project);

    /**
     * 修改分项工
     * @param projectItem 需要修改成的样子
     * @return 修改后的分项工程
     */
    ProjectItem updateProjectItem(ProjectItem projectItem);

    /**
     * 删除分项工程
     * @param projectItem 需要删除的分项工Id
     * @return  true表示删除成功，false表示删除失败
     */
    boolean deleteProjectItem(ProjectItem projectItem);
    Page<Project> onProcess(Pageable pageable);

    public Project get(int proId);
    public ProjectItem getItem(int proItemId);
    public List<Project> findProjectByBidTimeBetween(Date start, Date end);
    public List<Project> findProjectByWorkTime(Date start, Date end);
    public Page<Project> findAll(Pageable pageable);
    public Project getProjectByName(String name);
    public List<Project> getByProjectNameLike(String key);
    public ProjectItem getByItemName(Project project,String name);
    public Project getByProjectDetail(ProjectDetail projectDetail);
    public void updateProjectMaterialBegin(int projectId,int startIndex);
    public void updateProjectMaterialEnd(int projectId,int endIndex);
    public void updateProjectItemMaterialBegin(int projectItemId,int startIndex);
    public void updateProjectItemMaterialEnd(int projectItemId,int endIndex);
    public List<String> allProjectNames();
}
