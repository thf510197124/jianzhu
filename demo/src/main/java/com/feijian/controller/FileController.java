package com.feijian.controller;

import com.feijian.constant.CommonConstants;
import com.feijian.domain.Project;
import com.feijian.domain.FileDescription;
import com.feijian.domain.projectDetail.*;
import com.feijian.service.FileDescriptionService;
import com.feijian.service.ProjectDetailService;
import com.feijian.service.ProjectService;
import com.feijian.service.StatusService;
import com.feijian.utils.UserUtils;
import jdk.dynalink.beans.StaticClass;
import org.hibernate.type.TrueFalseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/file")
public class FileController {
    private static final String COMMON_FILE_PATH = CommonConstants.FILE_STORE_PATH_WIN + "commons\\";
    private static final String PROJECT_FILE_PATH = CommonConstants.FILE_STORE_PATH_WIN + "projects\\";
    StatusService statusService;
    ProjectService projectService;
    FileDescriptionService fileService;
    ProjectDetailService projectDetailService;

    @Autowired
    public FileController(StatusService statusService, ProjectService projectService,
                          FileDescriptionService fileService,ProjectDetailService projectDetailService) {
        this.statusService = statusService;
        this.projectService = projectService;
        this.fileService = fileService;
        this.projectDetailService = projectDetailService;
    }

    /**
     * 普通的表格controller，用于返回表格页面
     * @param projectId 用于查找文件的路径
     * @param statusId 是哪一个status
     * @param model model
     * @param session session
     * @return
     */
    @GetMapping("/status/addFile/{projectId}/{statusId}")
    public String uploadStatusFile(@PathVariable int projectId,@PathVariable int statusId, Model model, HttpSession session){
        FileDescription fileDes = new FileDescription();
        session.setAttribute("upload_status_id",statusId);
        session.setAttribute("upload_project_id",projectId);
        model.addAttribute("fileDes",fileDes);
        model.addAttribute("directory","status");
        return "/project/fileForm";
    }

    /**
     * 上传状态文件
     * @param file 上传的文件
     * @param fileDesc 文件描述
     * @param session session
     * @return
     * @throws IOException
     */
    @PostMapping("/status/addFile")
    public String uploadStatus(@RequestParam("file") MultipartFile file, FileDescription fileDesc,HttpSession session) throws IOException {
        int statusId = (int) session.getAttribute("upload_status_id");
        int projectId = (int)session.getAttribute("upload_project_id");
        session.removeAttribute("upload_status_id");
        session.removeAttribute("upload_ready_project_id");
        Project project = projectService.get(projectId);
        Status status = statusService.getStatus(statusId);
        FileDescription des = loadFileDes(file,fileDesc,project);
        des.setStatus(status);
        save(file, des);
        return "redirect:/file/status/fileList/" + statusId + "/" + 1;
    }

    /**
     * 为工程上传文件的form文件
     * @param projectId 工程的Id
     * @param model model
     * @param session session
     * @return 返回文件添加form页面
     */
    @GetMapping("/project/addFile/{projectId}")
    public String uploadProjectFile(@PathVariable int projectId,Model model,HttpSession session){
        session.setAttribute("upload_project_id",projectId);
        FileDescription fileDes = new FileDescription();
        model.addAttribute("fileDes",fileDes);
        model.addAttribute("directory","project");
        return "/project/fileForm";
    }

    /**
     * 为工程上传文件
     * @param file 上传的file
     * @param fileDes 文件描述
     * @param session session
     * @return 上传文件后的工程页面
     * @throws IOException 找不到存储的directory
     */
    @PostMapping("/project/addFile")
    public String addProjectFile(@RequestParam("file") MultipartFile file,FileDescription fileDes,HttpSession session,Model model) throws IOException {
        int projectId = (int)session.getAttribute("upload_project_id");
        if (file == null || file.getSize() > 10240*1000 || file.getSize() == 0){
            model.addAttribute("error","没有选择要上传的文件，或者选择的文件过大，不能上传");
            return uploadProjectFile(projectId,model,session);
        }
        session.removeAttribute("upload_project_id");
        Project project = projectService.get(projectId);
        FileDescription des = loadFileDes(file,fileDes,project);
        des.setProject(project);
        save(file, des);
        return "redirect:/file/project/fileList/" + projectId + "/" + 1;
    }

    /**
     * 展示project的文件列表
     * @param projectId 工程
     * @param pageNum 页面序列
     * @param model model
     * @param session session
     * @return 页面列表
     */
    @GetMapping("/project/fileList/{projectId}/{pageNum}")
    public String projectFileList(@PathVariable int projectId,@PathVariable int pageNum,Model model,HttpSession session){
        int pageSize = session.getAttribute("pageSize") == null? 10: (int) session.getAttribute("pageSize");
        Pageable pr = PageRequest.of(pageNum-1,pageSize);
        Project project = projectService.get(projectId);
        Page<FileDescription> page = fileService.getFilesByProject(project,pr);
        model.addAttribute("project",project);
        return forFileList(projectId,pageNum,pageSize,model,"工程相关文件",session,page,"project");
    }

    /**
     * 某一个status的文件列表
     * @param statusId status
     * @param pageNum 页码
     * @param model model
     * @param session session
     * @return 页面列表
     */
    @GetMapping("/status/fileList/{statusId}/{pageNum}")
    public String projectDetailList(@PathVariable int statusId,@PathVariable int pageNum,Model model,HttpSession session){
        int pageSize = session.getAttribute("pageSize") == null? 10: (int) session.getAttribute("pageSize");
        Pageable pr = PageRequest.of(pageNum-1,pageSize);
        Status status = statusService.getStatus(statusId);
        Page<FileDescription> page = statusService.getByStatus(status,pr);
        ProjectDetail projectDetail = projectDetailService.getProjectDetailByStatus(status);
        Project project = projectDetail.getProject();
        model.addAttribute("project",project);
        model.addAttribute("statusId",statusId);
        return forFileList(statusId,pageNum,pageSize,model,title(projectDetail,status),session,page,"status");
    }

    /**
     * 下载文件
     * @param fileId 下载的文件Id
     * @return 返回文件
     * @throws IOException 没有找到文件
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<InputStreamResource> download(@PathVariable int fileId) throws IOException{
        FileDescription fd = fileService.get(fileId);
        String entity = fd.getEntirety();
        String fileName = fd.getFileName() + fd.getSuffix();
        InputStreamResource isr = new InputStreamResource(new FileInputStream(entity));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("content-type", "application/octet-stream;charset=utf-8")
                .header("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8))
                .body(isr);
    }
    @GetMapping("/delete/{fileId}")
    public ResponseEntity<Boolean> deleteFile(@PathVariable int fileId){
        FileDescription fd = fileService.get(fileId);
        File file = new File(fd.getEntirety());
        if (file.delete()){
            fileService.delete(fd);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    private String title(ProjectDetail detail,Status status){
        String title;
        if (detail.getReadyStatus().getId() == status.getId()){
            title = "前期情况文件";
        }else if (detail.getProcessStatus().getId() == status.getId()){
            title = "竣工情况文件";
        }else if (detail.getOverStatus().getId() == status.getId()){
            title = "付款情况文件";
        }else if (detail.getPayStatus().getId() == status.getId()){
            title = "施工情况文件";
        }else{
            title = "工程情况文件";
        }
        return title;
    }

    private String forFileList(int id,int pageNum,int pageSize,Model model,String title,
                               HttpSession session,Page<FileDescription> page,String directory){
        session.setAttribute("pageSize",pageSize);
        session.setAttribute("pageNum",pageNum);
        model.addAttribute("page",page);
        model.addAttribute("title",title);
        model.addAttribute("specId",id);
        model.addAttribute("directory",directory);
        return "/project/fileList";
    }
    public FileDescription loadFileDes(MultipartFile file,FileDescription fileDesc,Project project) throws IOException {
        String path = getFileDirectory(project.getCode()) + "\\";
        String name = file.getOriginalFilename();
        assert name != null;
        fileDesc.setSuffix(name.substring(name.lastIndexOf(".")));
        fileDesc.setFileName(name.substring(0,name.lastIndexOf(".")));
        fileDesc.setEntirety(path + name);
        fileDesc.setPath(path);
        fileDesc.setUpdateTime(new Date());
        return fileDesc;
    }

    /**
     * 文件统一保存在D盘的jianzhu文件夹内
     * @param directory 建筑工程文件夹
     * @return 是否存在
     */
    private static String getFileDirectory(String directory) throws IOException {
        File file = new File(PROJECT_FILE_PATH + directory);
        if (!file.exists()){
            if (file.mkdir()){
                return file.getAbsolutePath();
            }else{
                throw new IOException("文件夹不存在！");
            }
        }
        return file.getAbsolutePath();
    }

    private void save(MultipartFile file, FileDescription des) throws IOException {
        if (des.getFileName().equals("")){
            String origin = Objects.requireNonNull(file).getOriginalFilename();
            assert origin != null;
            String name = origin.substring(0,origin.lastIndexOf("."));
            des.setFileName(name);
        }
        des.setUser(UserUtils.getUser());
        fileService.save(des);
        fileService.saveFile(des,file);
    }

}
