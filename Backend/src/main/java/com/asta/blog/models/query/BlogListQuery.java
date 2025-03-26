package com.asta.blog.models.query;

import lombok.*;
@Getter
@Setter
@ToString
public class BlogListQuery extends PageQuery{

    private String category;

    private String tags;

    private String keyword;

    private String status;
}
