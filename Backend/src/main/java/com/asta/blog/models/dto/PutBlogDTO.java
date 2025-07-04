package com.asta.blog.models.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PutBlogDTO {

    private Integer id;

    private String title;

    private String subtitle;

    private String category;

    private String readCount;

    private List<String> tags;

    private Integer status;

    private String content;
}
