package com.example.woodshop_studio.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import javax.persistence.*;
import java.sql.Timestamp;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user")
@TypeDef(
        name ="json",
        typeClass = JsonStringType.class
)
public class User {
    @GenericGenerator(name = "random_id", strategy = "com.example.woodshop_studio.model.custom.RandomIdGenerator")
    @Id
    @GeneratedValue(generator = "random_id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "email",nullable = false,length = 200)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "status",columnDefinition = "BOOLEAN")
    private boolean status;

    @Type(type = "json")
    @Column(name = "roles",nullable = false,columnDefinition = "json")
    private List<String> roles;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference
    private List<Token> tokens = new ArrayList<>();

    public User(String name, String email, String password, String address, String phone, List<String> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.roles = roles;
    }
}
