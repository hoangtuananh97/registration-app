package com.example.registrationapp.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Setter
@Getter
@Entity
@Table(name="user")
public class User {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "salary")
    private long salary;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserType userType;

    @Getter(AccessLevel.NONE)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    private boolean isEnabled;

    public User(){
        super();
        this.isEnabled = false;
    }
    public User(String username, String password, int salary, UserType userType) {
        this.username = username;
        this.password = password;
        this.salary = salary;
        this.userType = userType;
    }
    public User(String username, String password, int salary) {
        this.username = username;
        this.password = password;
        this.salary = salary;
    }

    public Collection<Role> getRoles() {
        return roles;
    }
}
