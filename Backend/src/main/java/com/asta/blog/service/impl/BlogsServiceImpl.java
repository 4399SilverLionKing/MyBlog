package com.asta.blog.service.impl;

import com.asta.blog.models.dto.PageDTO;
import com.asta.blog.models.entity.Blogs;
import com.asta.blog.mapper.BlogsMapper;
import com.asta.blog.models.query.BlogListQuery;
import com.asta.blog.models.dto.PostBlogDTO;
import com.asta.blog.models.dto.PutBlogDTO;
import com.asta.blog.service.IBlogsService;
import com.asta.blog.models.vo.BlogListVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Asta
 * @since 2025-03-23
 */
@Service
public class BlogsServiceImpl extends ServiceImpl<BlogsMapper, Blogs> implements IBlogsService {

    @Override
    public PageDTO<BlogListVO> getBlogList(BlogListQuery query) {
        return null;
    }

    @Override
    public Byte[] getBlogContent(Integer id) {
        return null;
    }

    @Override
    public void PostBlog(PostBlogDTO dto) {

    }

    @Override
    public void PutBlog(PutBlogDTO dto) {

    }

    @Override
    public void DeleteBlot(Integer id) {

    }
}
