package com.feijian.domain.projectDetail;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "process_status")
public class ProcessStatus extends Status {
    private String special;
    private String badWeather;
    //签证有多少份
    private int qianZheng;
    @OneToOne(targetEntity = ProjectDetail.class,mappedBy = "processStatus")
    @ToString.Exclude
    private ProjectDetail projectDetail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProcessStatus that = (ProcessStatus) o;
        return getId() != 0 && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
