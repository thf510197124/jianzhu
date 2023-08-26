package com.feijian.service;

import com.feijian.domain.FileDescription;
import com.feijian.domain.Project;
import com.feijian.domain.projectDetail.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StatusService {

    /**
     * 由于是单向关联，所以status保存后，必须更新pd;
     * @param status
     * @return
     */
    public ReadyStatus save(ReadyStatus status);
    public ProcessStatus save(ProcessStatus status);
    public OverStatus save(OverStatus status);
    public PayStatus save(PayStatus payStatus);
    public Status save(Status status);
    public ReadyStatus getReadyStatus(int id);
    public ProcessStatus getProcessStatus(int id);
    public OverStatus getOverStatus(int id);
    public PayStatus getPayStatus(int id);
    public Status getStatus(int id);
    public ReadyStatus getReadyStatusByProject(Project project);
    public ProcessStatus getProcessStatusByProject(Project project);
    public OverStatus getOverStatus(Project project);
    public PayStatus getPayStatus(Project project);

    public Page<FileDescription> getByStatus(Status status, Pageable pageable);
}
