package com.asta.blog.service;

import com.asta.blog.models.dto.PageDTO;
import com.asta.blog.models.entity.Blogs;
import com.asta.blog.models.query.BlogListQuery;
import com.asta.blog.models.dto.PostBlogDTO;
import com.asta.blog.models.dto.PutBlogDTO;
import com.asta.blog.models.vo.BlogListVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Asta
 * @since 2025-03-23
 */
public interface IBlogsService extends IService<Blogs> {
    /**
     * 获取博客列表
     * @param query 查询条件
     * @return 查询结果
     */
    public PageDTO<BlogListVO> getBlogList(BlogListQuery query);

    /**
     * 获取博客内容
     * @param id 博客id
     * @return md文件
     */
    public Byte[] getBlogContent(Integer id);

    /**
     * 添加博客
     * @param dto 博客内容
     */
    public void PostBlog(PostBlogDTO dto);

    /**
     * 修改博客
     * @param dto 修改内容
     */
    public void PutBlog(PutBlogDTO dto);

    /**
     * 删除博客
     * @param id 博客id
     */
    public void DeleteBlog(Integer id);

}
