package com.feijian.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String phone1;
    private String phone2;
    private String duties;
    @OneToMany(fetch = FetchType.LAZY,targetEntity = WareBill.class,mappedBy = "employee")
    private List<WareBill> billList;
    @OneToOne(targetEntity = User.class,mappedBy = "employee")
    User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    @Override
    public String toString(){
        return name;
    }
}
