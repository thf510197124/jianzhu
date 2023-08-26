package com.feijian.service.impl;

import com.feijian.dao.projectDetail.*;
import com.feijian.domain.projectDetail.*;
import com.feijian.service.ProjectDetailService;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDetailServiceImpl implements ProjectDetailService {
    ProjectDetailRepository pdDao;
    ReadyStatusRepository readyDao;
    ProcessStatusRepository processDao;
    OverStatusRepository overDao;
    PayStatusRepository payDao;
    StatusRepository statusDao;
    @Autowired
    public ProjectDetailServiceImpl(ProjectDetailRepository pdDao,ReadyStatusRepository readyDao,
                                    ProcessStatusRepository processDao,OverStatusRepository overDao,
                                    PayStatusRepository payDao,StatusRepository statusDao) {
        this.pdDao = pdDao;
        this.readyDao = readyDao;
        this.processDao = processDao;
        this.overDao = overDao;
        this.payDao = payDao;
        this.statusDao = statusDao;
    }

    @Override
    public ProjectDetail saveProjectDetail(ProjectDetail projectDetail) {
        return pdDao.save(projectDetail);
    }

    @Override
    public ProjectDetail get(int projectDetailId) {
        return pdDao.getOne(projectDetailId);
    }

    @Override
    public ProjectDetail getProjectDetailByStatus(Status status) {
        return pdDao.getProjectDetailByStatus(status.getId());
    }
}
