package com.feijian.domain;

import com.feijian.domain.projectDetail.Status;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "file_system")
public class FileDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //例如中标通知书，工程合同等等,分类
    private String fileType;
    //文件名，可以与fileType不一致
    private String fileName;
    //后缀
    private String suffix;
    //路径
    private String path;
    //包含路径的完整名称
    private String entirety;
    //介绍，可能是对情况的说明
    private String detail;
    @Temporal(value=TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    //status是工程状态
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Status.class)
    @JoinColumn(name = "status_id",referencedColumnName = "id")
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Project.class)
    @JoinColumn(name = "project_id",referencedColumnName = "id")
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = User.class)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
}
