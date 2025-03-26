package com.asta.blog.service.impl;

import com.asta.blog.controller.LoginController;
import com.asta.blog.mapper.MsBlogsMapper;
import com.asta.blog.models.dto.PageDTO;
import com.asta.blog.models.entity.Blogs;
import com.asta.blog.mapper.BlogsMapper;
import com.asta.blog.models.query.BlogListQuery;
import com.asta.blog.models.dto.PostBlogDTO;
import com.asta.blog.models.dto.PutBlogDTO;
import com.asta.blog.service.IBlogsService;
import com.asta.blog.models.vo.BlogListVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MsBlogsMapper ms;

    @Override
    public PageDTO<BlogListVO> getBlogList(BlogListQuery query) {
        Page<Blogs> blogsPage = baseMapper.SelectByQuery(query);
        return PageDTO.create(blogsPage,ms::toBlogListVO);
    }

    @Override
    public Byte[] getBlogContent(Integer id) {
        return null;
    }

    @Override
    public void PostBlog(PostBlogDTO dto) {
        Blogs blog = ms.PostDTOtoBlog(dto);
        blog.setDate(LocalDateTime.now());
        blog.setReadCount(0);
        baseMapper.insert(blog);
    }

    @Override
    public void PutBlog(PutBlogDTO dto) {
        Blogs blog = ms.PutDTOtoBlog(dto);
        blog.setDate(LocalDateTime.now());
        baseMapper.updateById(blog);
    }

    @Override
    public void DeleteBlog(Integer id) {
        baseMapper.deleteById(id);
    }
}
