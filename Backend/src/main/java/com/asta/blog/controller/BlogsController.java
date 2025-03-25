package com.asta.blog.controller;

import com.asta.blog.controller.apis.BlogsApi;
import com.asta.blog.models.dto.PageDTO;
import com.asta.blog.models.dto.PostBlogDTO;
import com.asta.blog.models.dto.PutBlogDTO;
import com.asta.blog.models.query.BlogListQuery;
import com.asta.blog.service.impl.BlogsServiceImpl;
import com.asta.blog.models.vo.BlogListVO;
import com.asta.blog.models.vo.JsonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Asta
 * @since 2025-03-23
 */
@RestController
@RequestMapping("/blog/blogs")
public class BlogsController implements BlogsApi {

    @Autowired
    BlogsServiceImpl service;

    @Override
    public JsonVO<PageDTO<BlogListVO>> getBlogList(BlogListQuery query) {
        return null;
    }

    @Override
    public JsonVO<Byte[]> getBlogContent(Integer id) {
        return null;
    }

    @Override
    public JsonVO<Object> PostBlog(PostBlogDTO dto) {
        return null;
    }

    @Override
    public JsonVO<Object> PutBlog(PutBlogDTO dto) {
        return null;
    }

    @Override
    public JsonVO<Object> DeleteBlot(Integer id) {
        return null;
    }
}
