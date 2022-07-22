package com.example.registrationapp.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="user_type")
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "type_classify")
    private String typeClassify;

    @Column(name = "from_salary")
    private long fromSalary;

    @Column(name = "to_salary")
    private long toSalary;

    @Column(name = "is_limit_salary")
    private boolean isLimitSalary = true;

    @OneToMany(mappedBy = "userType", targetEntity = User.class, orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<User> users;

    public UserType(String typeClassify, long fromSalary, long toSalary) {
        this.typeClassify = typeClassify;
        this.fromSalary = fromSalary;
        this.toSalary = toSalary;
    }

}
