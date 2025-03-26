package com.asta.blog.mapper;

import com.asta.blog.models.dto.PostBlogDTO;
import com.asta.blog.models.dto.PutBlogDTO;
import com.asta.blog.models.entity.Blogs;
import com.asta.blog.models.vo.BlogListVO;
import com.asta.blog.utils.AttributeConvertUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = AttributeConvertUtil.class)
public interface MsBlogsMapper {

    @Mapping(source = "tags", target = "tags", qualifiedByName = "stringToList")
    BlogListVO toBlogListVO(Blogs blogs);

    @Mapping(source = "tags",target = "tags", qualifiedByName = "listToString")
    Blogs PostDTOtoBlog(PostBlogDTO dto);

    @Mapping(source = "tags",target = "tags", qualifiedByName = "listToString")
    Blogs PutDTOtoBlog(PutBlogDTO dto);
}
