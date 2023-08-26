package com.feijian.controller;

import com.feijian.domain.Project;
import com.feijian.domain.projectDetail.*;
import com.feijian.service.ProjectDetailService;
import com.feijian.service.ProjectService;
import com.feijian.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/project_detail")
public class ProjectDetailController {
    ProjectService projectService;
    ProjectDetailService projectDetailService;
    StatusService statusService;

    @Autowired
    public ProjectDetailController(ProjectService projectService,
                                   ProjectDetailService projectDetailService,
                                   StatusService statusService) {
        this.projectService = projectService;
        this.projectDetailService = projectDetailService;
        this.statusService = statusService;
    }


    /**
     * 为project添加Ready的页面
     * @param projectId project
     * @param model model
     * @return 添加Ready的form
     */
    @GetMapping("/addReady/{projectId}")
    public String addReady(@PathVariable int projectId, Model model){
        ReadyStatus status = new ReadyStatus();
        addModelForForm(projectId,status,model);
        return "/project/pro_detail/readyForm";
    }
    /**
     * 为project添加Process的页面
     * @param projectId project
     * @param model model
     * @return 添加process的form
     */
    @GetMapping("/addProcess/{projectId}")
    public String addProcess(@PathVariable int projectId, Model model){
        ProcessStatus status = new ProcessStatus();
        addModelForForm(projectId,status,model);
        return "/project/pro_detail/processForm";
    }

    /**
     * 为project添加Over的页面
     * @param projectId project
     * @param model model
     * @return 添加Over的form
     */
    @GetMapping("/addOver/{projectId}")
    public String addOver(@PathVariable int projectId, Model model){
        OverStatus status = new OverStatus();
        addModelForForm(projectId,status,model);
        return "/project/pro_detail/overForm";
    }
    /**
     * 为project添加Pay的页面
     * @param projectId project
     * @param model model
     * @return 添加Over的form
     */
    @GetMapping("/addPay/{projectId}")
    public String addPay(@PathVariable int projectId, Model model){
        PayStatus status = new PayStatus();
        addModelForForm(projectId,status,model);
        return "/project/pro_detail/payForm";
    }

    /**
     * Ready的添加Post
     * @param projectId project
     * @param status ready
     * @return 展示页面
     */
    @PostMapping("/addReady/{projectId}")
    public String addReadyForm(@PathVariable int projectId,ReadyStatus status){
        ProjectDetail projectDetail = getProjectDetail(projectId);
        if (projectDetail.getReadyStatus() != null){//更新的话，这样更新
            ReadyStatus org = projectDetail.getReadyStatus();
            status.setId(org.getId());
        }
        projectDetail.setReadyStatus(status);
        projectDetailService.saveProjectDetail(projectDetail);
        /*status = projectDetail.getReadyStatus();
        setAfterPostStatus(status,projectId,model,session);*/
        return "redirect:/project_detail/ready/"+ projectId;
    }
    /**
     * Process的添加Post
     * @param projectId project
     * @param status ready
     * @return 展示页面
     */
    @PostMapping("/addProcess/{projectId}")
    public String addProcessForm(@PathVariable int projectId,ProcessStatus status){
        ProjectDetail projectDetail = getProjectDetail(projectId);
        if (projectDetail.getProcessStatus() != null){
            ProcessStatus org = projectDetail.getProcessStatus();
            status.setId(org.getId());
        }
        projectDetail.setProcessStatus(status);
        projectDetailService.saveProjectDetail(projectDetail);
        /*status = projectDetail.getProcessStatus();
        setAfterPostStatus(status,projectId,model,session);
        return "/project/pro_detail/process";*/
        return "redirect:/project_detail/process/"+ projectId;
    }
    /**
     * Over的添加Post
     * @param projectId project
     * @param status ready
     * @return 展示页面
     */
    @PostMapping("/addOver/{projectId}")
    public String addOverForm(@PathVariable int projectId,OverStatus status){
        ProjectDetail projectDetail = getProjectDetail(projectId);
        if (projectDetail.getOverStatus() != null){
            OverStatus org = projectDetail.getOverStatus();
            status.setId(org.getId());
        }
        projectDetail.setOverStatus(status);
        projectDetailService.saveProjectDetail(projectDetail);
        /*status = projectDetail.getOverStatus();
        setAfterPostStatus(status,projectId,model,session);
        return "/project/pro_detail/over";*/
        return "redirect:/project_detail/over/"+ projectId;
    }
    /**
     * Over的添加Post
     * @param projectId project
     * @param status ready
     * @return 展示页面
     */
    @PostMapping("/addPay/{projectId}")
    public String addPayForm(@PathVariable int projectId,PayStatus status){
        ProjectDetail projectDetail = getProjectDetail(projectId);
        if (projectDetail.getPayStatus() != null){
            PayStatus org = projectDetail.getPayStatus();
            status.setId(org.getId());
        }
        projectDetail.setPayStatus(status);
        projectDetailService.saveProjectDetail(projectDetail);
        /*status = projectDetail.getPayStatus();
        setAfterPostStatus(status,projectId,model,session);
        return "/project/pro_detail/pay";*/
        return "redirect:/project_detail/pay/"+ projectId;
    }


    /**
     * 里面的一个特殊处理是：没有添加，自动转到添加页面
     * ready的展示详情页面
     * @param projectId project
     * @param model model
     * @param session session
     * @return ready展示页面
     */
    @GetMapping("/ready/{projectId}")
    public String ready(@PathVariable int projectId, Model model, HttpSession session){
        //为了应对页面上出现的查看status不存在的情况。
        ReadyStatus status = getProjectDetail(projectId).getReadyStatus();
        if (status == null){
            return "redirect:/project_detail/addReady/" + projectId;
        }
        setStatusDetailModel(projectId,status,model,session);
        return "/project/pro_detail/ready";
    }
    /**
     * 里面的一个特殊处理是：没有添加，自动转到添加页面
     * process的展示详情页面
     * @param projectId project
     * @param model model
     * @param session session
     * @return ready展示页面
     */
    @GetMapping("/process/{projectId}")
    public String process(@PathVariable int projectId, Model model, HttpSession session){
        //为了应对页面上出现的查看status不存在的情况。
        ProcessStatus status = getProjectDetail(projectId).getProcessStatus();
        if (status == null){
            return "redirect:/project_detail/addProcess/" + projectId;
        }
        setStatusDetailModel(projectId,status,model,session);
        return "/project/pro_detail/process";
    }
    /**
     * 里面的一个特殊处理是：没有添加，自动转到添加页面
     * process的展示详情页面
     * @param projectId project
     * @param model model
     * @param session session
     * @return ready展示页面
     */
    @GetMapping("/over/{projectId}")
    public String over(@PathVariable int projectId, Model model, HttpSession session){
        //为了应对页面上出现的查看status不存在的情况。
        OverStatus status = getProjectDetail(projectId).getOverStatus();
        if (status == null){
            return "redirect:/project_detail/addOver/" + projectId;
        }
        setStatusDetailModel(projectId,status,model,session);
        return "/project/pro_detail/over";
    }
    /**
     * 里面的一个特殊处理是：没有添加，自动转到添加页面
     * process的展示详情页面
     * @param projectId project
     * @param model model
     * @param session session
     * @return ready展示页面
     */
    @GetMapping("/pay/{projectId}")
    public String pay(@PathVariable int projectId, Model model, HttpSession session){
        //为了应对页面上出现的查看status不存在的情况。
        PayStatus status = getProjectDetail(projectId).getPayStatus();
        if (status == null){
            return "redirect:/project_detail/addPay/" + projectId;
        }
        setStatusDetailModel(projectId,status,model,session);
        return "/project/pro_detail/pay";
    }

    /**
     * Ready的更改页面
     * @param readyId readyId
     * @param model model
     * @param session session
     * @return
     */
    @GetMapping("/update/ready/{readyId}")
    public String updateReady(@PathVariable int readyId,Model model,HttpSession session){
        ReadyStatus status = statusService.getReadyStatus(readyId);
        int projectId = (int) session.getAttribute("projectId");
        setUpdateModel(projectId,status,model,session);
        return "/project/pro_detail/readyForm";
    }
    /**
     * Process的更改页面
     * @param readyId readyId
     * @param model model
     * @param session session
     * @return
     */
    @GetMapping("/update/process/{readyId}")
    public String updateProcess(@PathVariable int readyId,Model model,HttpSession session){
        ProcessStatus status = statusService.getProcessStatus(readyId);
        int projectId = (int) session.getAttribute("projectId");
        setUpdateModel(projectId,status,model,session);
        return "/project/pro_detail/processForm";
    }
    /**
     * Process的更改页面
     * @param readyId readyId
     * @param model model
     * @param session session
     * @return
     */
    @GetMapping("/update/over/{readyId}")
    public String updateOver(@PathVariable int readyId,Model model,HttpSession session){
        OverStatus status = statusService.getOverStatus(readyId);
        int projectId = (int) session.getAttribute("projectId");
        setUpdateModel(projectId,status,model,session);
        return "/project/pro_detail/overForm";
    }
    /**
     * Process的更改页面
     * @param readyId readyId
     * @param model model
     * @param session session
     * @return
     */
    @GetMapping("/update/pay/{readyId}")
    public String updatePay(@PathVariable int readyId,Model model,HttpSession session){
        PayStatus status = statusService.getPayStatus(readyId);
        int projectId = (int) session.getAttribute("projectId");
        setUpdateModel(projectId,status,model,session);
        return "/project/pro_detail/payForm";
    }

    private ProjectDetail getProjectDetail(int projectId){
        Project project = projectService.get(projectId);
        ProjectDetail projectDetail = project.getProjectDetail();
        if (projectDetail == null){
            projectDetail = new ProjectDetail();
            project.setProjectDetail(projectDetail);
            projectService.updateProject(project);
        }
        return project.getProjectDetail();
    }
    private Project getProject(int projectId){
        return projectService.get(projectId);
    }
    //为获取页面添加model
    private void addModelForForm(int projectId, Status status,Model model){
        model.addAttribute("projectId",projectId);
        model.addAttribute("status",status);
        model.addAttribute("projectName",getProject(projectId).getProjectName());
        model.addAttribute("attr","添加");
    }
    //为添加status后添加model
    private void setAfterPostStatus(Status status,int projectId,Model model,HttpSession session){
        session.setAttribute("projectId",projectId);
        model.addAttribute("status",status);
    }
    //为status展示页面添加model
    private void setStatusDetailModel(int projectId,Status status,Model model,HttpSession session){
        model.addAttribute("projectId",projectId);
        model.addAttribute("status",status);
        model.addAttribute("projectName",getProject(projectId).getProjectName());
        session.setAttribute("projectId",projectId);
    }
    //为更新status添加model
    private void setUpdateModel(int projectId,Status status,Model model,HttpSession session){
        session.removeAttribute("projectId");
        model.addAttribute("projectId",projectId);
        model.addAttribute("status",status);
        model.addAttribute("attr","更新");
    }
}
