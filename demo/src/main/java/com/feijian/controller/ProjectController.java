package com.feijian.controller;

import com.feijian.domain.Company;
import com.feijian.domain.Project;
import com.feijian.domain.ProjectItem;
import com.feijian.domain.User;
import com.feijian.domain.projectDetail.ProjectDetail;
import com.feijian.service.ProjectService;
import com.feijian.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {
    ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all/{pageNum}")
    public String allProject(@PathVariable int pageNum,Model model,HttpSession session){
        int pageSize = session.getAttribute("projectPageSize") == null? 10: (int) session.getAttribute("projectPageSize");
        session.setAttribute("projectPageSize",pageSize);
        PageRequest pr = PageRequest.of(pageNum-1,pageSize);
        session.setAttribute("projectPageNum",pageNum);
        Page<Project> page = projectService.findAll(pr);
        model.addAttribute("page",page);
        return "/project/projectList";
    }


    @GetMapping("/{projectId}")
    public String projectDetail(@PathVariable("projectId") int projectId,Model model){
        Project project = projectService.get(projectId);
        model.addAttribute("project",project);
        model.addAttribute("contractDays",dateDuring(project.getContractEnd(),project.getContractBegin()));
        model.addAttribute("processDays",dateDuring(project.getProcessEnd(),project.getProcessBegin()));
        ProjectDetail pd = project.getProjectDetail();
        if (pd == null){
            model.addAttribute("ready",0);model.addAttribute("process",0);model.addAttribute("over",0);model.addAttribute("pay",0);
        }else{
            model.addAttribute( "ready",pd.getReadyStatus() == null ? 0:1);
            model.addAttribute( "process",pd.getProcessStatus() == null ? 0:1);
            model.addAttribute( "over",pd.getOverStatus() == null ? 0:1);
            model.addAttribute( "pay",pd.getPayStatus() == null ? 0:1);
        }
        List<ProjectItem> items = project.getProjectItems();
        if (items == null){
            ProjectItem item = createItem();
            item.setProject(project);
            projectService.updateProjectItem(item);
            items = project.getProjectItems();
        }
        User u = project.getUser();
        model.addAttribute("u",u);
        model.addAttribute("items",items);
        return "/project/project2";
    }

    @GetMapping("/add")
    public String addProject(Model model ,HttpSession session){
        Project project = new Project();
        model.addAttribute("project",project);
        model.addAttribute("attr","添加");
        session.setAttribute("project_projectId",0);
        return "/project/projectForm";
    }
    @GetMapping("/update/{projectId}")
    public String updateProject(@PathVariable int projectId,Model model,HttpSession session){
        Project project = projectService.get(projectId);
        session.setAttribute("project_projectId",projectId);
        model.addAttribute("attr","修改");
        model.addAttribute("project",project);
        return "/project/projectForm";
    }
    @PostMapping("/add")
    public String addProject(Project project,HttpSession session){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        project.setUser(user);
        project.setAddTime(new Date());
        int id = (int) session.getAttribute("project_projectId");
        if (id != 0){
            project.setId(id);
        }
        project = projectService.createProject(project);
        return "redirect:/project/" + project.getId();
    }
    @GetMapping("/onProcess/{pageNum}")
    public String OnProcess(@PathVariable int pageNum,Model model,HttpSession session){
        int pageSize = session.getAttribute("projectPageSize") == null? 10: (int) session.getAttribute("projectPageSize");
        session.setAttribute("projectPageSize",pageSize);
        PageRequest pr = PageRequest.of(pageNum-1,pageSize);
        session.setAttribute("projectPageNum",pageNum);
        Page<Project> page = projectService.onProcess(pr);
        model.addAttribute("page",page);
        return "/project/projectList";
    }
    @GetMapping("/addItem/{projectId}")
    public String addProjectItem(@PathVariable int projectId, Model model, HttpSession session){
        ProjectItem proItem = new ProjectItem();
        model.addAttribute("proItem",proItem);
        session.setAttribute("addItem_projectId",projectId);
        return "/project/projectItemForm";
    }
    @PostMapping("/addItem")
    public String addItem(ProjectItem proItem,Model model,HttpSession session){
        SecurityContext context = SecurityContextHolder.getContext();
        User user = (User) context.getAuthentication().getPrincipal();
        proItem.setUser(user);

        int projectId = (int) session.getAttribute("addItem_projectId");
        session.removeAttribute("addItem_projectId");
        Project project = projectService.get(projectId);
        proItem.setProject(project);

        projectService.updateProjectItem(proItem);
        model.addAttribute("proItem",proItem);
        model.addAttribute("project",project);
        return "/project/projectItem";
    }
    @GetMapping("/projectItem/{projectItemId}")
    public String projectItem(@PathVariable int projectItemId,Model model,HttpSession session){
        ProjectItem projectItem = projectService.getItem(projectItemId);
        model.addAttribute("proItem",projectItem);
        Project project = projectItem.getProject();
        model.addAttribute("project",project);
        session.setAttribute("projectId_for_ware",project.getId());
        session.setAttribute("projectItemId_for_ware",projectItem.getId());
        return "/project/projectItem";
    }
    @GetMapping("/allProjectItem/{projectId}")
    public String allProjectItem(@PathVariable int projectId,Model model){
        Project project = projectService.get(projectId);
        List<ProjectItem> projectItems = project.getProjectItems();
        model.addAttribute("projectItems",projectItems);
        return "/project/projectItemList";
    }
    @RequestMapping(value = "/projectItem",method = RequestMethod.POST)
    public ResponseEntity<List<String>> getCompanyNameList(@RequestBody String project){
        Project project1 = projectService.getProjectByName(project);
        List<ProjectItem> items = project1.getProjectItems();
        List<String> names = new ArrayList<>();
        for (ProjectItem item : items){
            names.add(item.getItemName());
        }
        return new ResponseEntity<>(names, HttpStatus.OK);
    }
    private long dateDuring(Date date1,Date date2){
        if (date1 == null || date2 == null){
            return 0;
        }else{
            return Math.abs(date1.getTime() - date2.getTime())/DateUtils.MILLISECONDS_PER_DAY;
        }
    }
    private ProjectItem createItem(){
        ProjectItem item = new ProjectItem();
        item.setItemName("本工程");
        return item;
    }
}
