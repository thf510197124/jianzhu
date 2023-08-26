package com.feijian.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "contactor")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contactor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String phone1;
    private String phone2;
    private String phone3;
    /**
     * 职位
     */
    private String duties;
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Company.class)
    @JoinColumn(name = "company",referencedColumnName = "id")
    @ToString.Exclude
    private Company company;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Contactor contactor = (Contactor) o;
        return Objects.equals(id, contactor.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
