package com.asta.blog.models.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetBlogContentVO {

    private String url;

    private long expires;
}
