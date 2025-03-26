package com.asta.blog.controller.apis;

import com.asta.blog.models.dto.PageDTO;
import com.asta.blog.models.dto.PostBlogDTO;
import com.asta.blog.models.dto.PutBlogDTO;
import com.asta.blog.models.query.BlogListQuery;
import com.asta.blog.models.vo.BlogListVO;
import com.asta.blog.models.vo.JsonVO;

public interface BlogsApi {
    /**
     * 获取博客列表
     * @param query 查询条件
     * @return 查询结果
     */
    public JsonVO<PageDTO<BlogListVO>> getBlogList(BlogListQuery query);

    /**
     * 获取博客内容
     * @param id 博客id
     * @return md文件
     */
    public JsonVO<Byte[]> getBlogContent(Integer id);

    /**
     * 添加博客
     * @param dto 博客内容
     * @return null
     */
    public JsonVO<Object> PostBlog(PostBlogDTO dto);

    /**
     * 修改博客
     * @param dto 修改内容
     * @return null
     */
    public JsonVO<Object> PutBlog(PutBlogDTO dto);

    /**
     * 删除博客
     * @param id 博客id
     * @return null
     */
    public JsonVO<Object> DeleteBlog(Integer id);
}
