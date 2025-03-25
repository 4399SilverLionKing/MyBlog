package com.asta.blog.models.vo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class BlogListVO {

    private Integer id;

    private String title;

    private String desc;

    private String category;

    private LocalDateTime date;

    private Integer readCount;

    private List<String> tags;

    private String status;
}
