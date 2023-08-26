package com.feijian.domain.projectDetail;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "pay_status")
public class PayStatus extends Status {

    @Column(name = "contract_pay_method")
    private String contractPayMethod;
    @Column(name = "payment_method")
    private String paymentMethod;
    @OneToOne(targetEntity = ProjectDetail.class,mappedBy = "payStatus")
    @ToString.Exclude
    private ProjectDetail projectDetail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PayStatus payStatus = (PayStatus) o;
        return getId() != 0 && Objects.equals(getId(), payStatus.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
