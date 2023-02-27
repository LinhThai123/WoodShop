package com.example.woodshop_studio.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "image")
@Table
@Builder
public class Image implements Serializable {

    @GenericGenerator(name = "random_id", strategy = "com.example.woodshop_studio.model.custom.RandomIdGenerator")
    @Id
    @GeneratedValue(generator = "random_id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "link", unique = true)
    private String link;

    @Column(name = "size")
    private long size;

    @Column(name = "uploaded_at")
    private Timestamp uploadedAt;

    @Column(name = "created_by")
    private String createdBy;
}
