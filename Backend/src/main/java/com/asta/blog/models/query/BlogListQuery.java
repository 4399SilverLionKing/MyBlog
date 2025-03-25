package com.asta.blog.models.query;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class BlogListQuery extends PageQuery{

    private String category;

    private List<String> tags;

    private String keyword;

    private String status;
}
