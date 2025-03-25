package com.asta.blog.mapper;

import com.asta.blog.models.entity.Blogs;
import com.asta.blog.models.vo.BlogListVO;
import com.asta.blog.utils.AttributeConvertUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = AttributeConvertUtil.class)
public interface MsBlogsMapper {

    @Mapping(source = "tags",target = "tags",qualifiedByName = "strToList")
    BlogListVO toBlogListVO(Blogs blogs);
}
