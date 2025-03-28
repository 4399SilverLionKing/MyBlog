package com.asta.blog.controller;

import com.asta.blog.models.dto.PageDTO;
import com.asta.blog.models.dto.PostBlogDTO;
import com.asta.blog.models.dto.PutBlogDTO;
import com.asta.blog.models.query.BlogListQuery;
import com.asta.blog.models.vo.GetBlogContentVO;
import com.asta.blog.models.vo.PostBlogVO;
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
public class BlogsController{

    @Autowired
    BlogsServiceImpl service;

    @GetMapping("public/blogs")
    public JsonVO<PageDTO<BlogListVO>> getBlogList(BlogListQuery query) {
        return JsonVO.success(service.getBlogList(query)) ;
    }

    @GetMapping("public/blogs/{key}")
    public JsonVO<GetBlogContentVO> getBlogContent(@PathVariable String key) {
        return JsonVO.success(service.getBlogContent(key));
    }

    @PostMapping("/blogs")
    public JsonVO<PostBlogVO> PostBlog(@RequestBody PostBlogDTO dto) {
        return JsonVO.success(service.PostBlog(dto));
    }

    @PutMapping("/blogs")
    public JsonVO<String> PutBlog(@RequestBody PutBlogDTO dto) {
        return JsonVO.success(service.PutBlog(dto));
    }

    @DeleteMapping("/blogs/{id}")
    public JsonVO<Object> DeleteBlog(@PathVariable Integer id) {
        service.DeleteBlog(id);
        return JsonVO.success(null);
    }
}
