package com.feijian.domain;

import javax.persistence.*;

import com.feijian.item.CompanyType;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String companyName;
    private String address;
    private String owner;
    private String taxNumber;
    private String bank;
    private String accountCode;
    @Enumerated(value = EnumType.STRING)
    private CompanyType type;
    private String description;
    @OneToMany(fetch = FetchType.LAZY,targetEntity = Contactor.class,
            cascade = CascadeType.REMOVE,mappedBy = "company")
    private List<Contactor> contactors;
    private String others;
    @Override
    public String toString(){
        return companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return id == company.id && Objects.equals(companyName, company.companyName) && Objects.equals(address, company.address) && Objects.equals(owner, company.owner) && Objects.equals(taxNumber, company.taxNumber) && Objects.equals(bank, company.bank) && Objects.equals(accountCode, company.accountCode) && type == company.type && Objects.equals(description, company.description) && Objects.equals(contactors, company.contactors) && Objects.equals(others, company.others);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName, address, owner, taxNumber, bank, accountCode, type, description, contactors, others);
    }
}
