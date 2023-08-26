package com.feijian.domain.projectDetail;

import com.feijian.domain.Project;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "project_detail")
public class ProjectDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "ready_status_id",referencedColumnName = "id")
    @ToString.Exclude
    private ReadyStatus readyStatus;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "process_status_id",referencedColumnName = "id")
    @ToString.Exclude
    private ProcessStatus processStatus;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "over_status_id",referencedColumnName = "id")
    @ToString.Exclude
    private OverStatus overStatus;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_status_id",referencedColumnName = "id")
    @ToString.Exclude
    private PayStatus payStatus;

    @OneToOne(mappedBy = "projectDetail",targetEntity = Project.class,fetch = FetchType.EAGER)
    private Project project;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProjectDetail that = (ProjectDetail) o;
        return id != 0 && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
