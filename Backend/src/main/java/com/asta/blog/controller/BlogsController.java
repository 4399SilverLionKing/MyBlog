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
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Asta
 * @since 2025-03-23
 */
@RestController
public class BlogsController implements BlogsApi {

    @Autowired
    BlogsServiceImpl service;

    @Override
    @GetMapping("public/blogs")
    public JsonVO<PageDTO<BlogListVO>> getBlogList(BlogListQuery query) {
        return JsonVO.success(service.getBlogList(query)) ;
    }

    @Override
    @GetMapping("public/blogs/{id}")
    public JsonVO<Byte[]> getBlogContent(@PathVariable Integer id) {
        return null;
    }

    @Override
    @PostMapping("/blogs")
    public JsonVO<Object> PostBlog(@RequestBody PostBlogDTO dto) {
        service.PostBlog(dto);
        return JsonVO.success(null);
    }

    @Override
    @PutMapping("/blogs")
    public JsonVO<Object> PutBlog(@RequestBody PutBlogDTO dto) {
        service.PutBlog(dto);
        return JsonVO.success(null);
    }

    @Override
    @DeleteMapping("/blogs/{id}")
    public JsonVO<Object> DeleteBlog(@PathVariable Integer id) {
        service.DeleteBlog(id);
        return JsonVO.success(null);
    }
}
