package com.feijian.service.impl;

import com.feijian.dao.ProjectRepository;
import com.feijian.dao.projectDetail.*;
import com.feijian.domain.FileDescription;
import com.feijian.domain.Project;
import com.feijian.domain.projectDetail.ProjectDetail;
import com.feijian.domain.projectDetail.*;
import com.feijian.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements StatusService {
    ReadyStatusRepository readyDao;
    ProcessStatusRepository processDao;
    OverStatusRepository overDao;
    PayStatusRepository payDao;
    ProjectDetailRepository pdDao;
    ProjectRepository pDao;
    StatusRepository statusDao;
    FileDescriptionRepository fileDao;
    @Autowired
    public StatusServiceImpl(ReadyStatusRepository readyDao, ProcessStatusRepository processDao,
                             OverStatusRepository overDao, PayStatusRepository payDao,
                             ProjectDetailRepository pdDao,FileDescriptionRepository fileDao,
                             ProjectRepository pDao,StatusRepository statusDao) {
        this.readyDao = readyDao;
        this.processDao = processDao;
        this.overDao = overDao;
        this.payDao = payDao;
        this.pdDao = pdDao;
        this.pDao = pDao;
        this.statusDao = statusDao;
        this.fileDao = fileDao;
    }

    @Override
    public ReadyStatus save(ReadyStatus status){
        //readyDao.save(status);//由于是CascadeType.ALL,所以可以不用这句
        readyDao.save(status);
        return status;
    }

    @Override
    public ProcessStatus save(ProcessStatus status){
        processDao.save(status);
        return status;
    }

    @Override
    public OverStatus save(OverStatus status) {
        overDao.save(status);
        return status;
    }

    @Override
    public PayStatus save(PayStatus payStatus) {
        payDao.save(payStatus);
        return payStatus;
    }

    @Override
    public Status save(Status status) {
        return statusDao.save(status);
    }

    @Override
    public ReadyStatus getReadyStatus(int id) {
        return readyDao.getOne(id);
    }

    @Override
    public ProcessStatus getProcessStatus(int id) {
        return processDao.getOne(id);
    }

    @Override
    public OverStatus getOverStatus(int id) {
        return overDao.getOne(id);
    }

    @Override
    public PayStatus getPayStatus(int id) {
        return payDao.getOne(id);
    }

    @Override
    public Status getStatus(int id) {
        return statusDao.getOne(id);
    }

    @Override
    public ReadyStatus getReadyStatusByProject(Project project) {
        if (project.getId() == 0){
            throw new RuntimeException("必须是保存过的project");
        }
        Project p1 = pDao.getOne(project.getId());
        ProjectDetail pd = p1.getProjectDetail();
        return pd.getReadyStatus();
    }

    @Override
    public ProcessStatus getProcessStatusByProject(Project project) {
        if (project.getId() == 0){
            throw new RuntimeException("必须是保存过的project");
        }
        Project p1 = pDao.getOne(project.getId());
        ProjectDetail pd = p1.getProjectDetail();
        return pd.getProcessStatus();
    }

    @Override
    public OverStatus getOverStatus(Project project) {
        if (project.getId() == 0){
            throw new RuntimeException("必须是保存过的project");
        }
        Project p1 = pDao.getOne(project.getId());
        ProjectDetail pd = p1.getProjectDetail();
        return pd.getOverStatus();
    }

    @Override
    public PayStatus getPayStatus(Project project) {
        if (project.getId() == 0){
            throw new RuntimeException("必须是保存过的project");
        }
        Project p1 = pDao.getOne(project.getId());
        ProjectDetail pd = p1.getProjectDetail();
        return pd.getPayStatus();
    }

    @Override
    public Page<FileDescription> getByStatus(Status status, Pageable pageable) {
        return fileDao.getFileDescriptionsByStatus(status,pageable);
    }

}
