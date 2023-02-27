package com.example.woodshop_studio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private String id;

    private String slug;

    private String title;

    private String createdAt;

    private String publishedAt;

    private String status;

}
