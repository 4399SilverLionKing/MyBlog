package com.asta.blog.models.vo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class BlogListVO {

    private Integer id;

    private String title;

    private String desc;

    private String category;

    private String date;

    private String readCount;

    private List<String> tags;

    private String status;
}
