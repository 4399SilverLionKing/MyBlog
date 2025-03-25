package com.asta.blog.mapper;

import com.asta.blog.models.entity.Blogs;
import com.asta.blog.models.query.BlogListQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Asta
 * @since 2025-03-23
 */
@Mapper
public interface BlogsMapper extends BaseMapper<Blogs> {

    default Page<Blogs> SelectByQuery(BlogListQuery query){
        //构建分页对象
        Page<Blogs> page = new Page<>(query.getPageIndex(), query.getPageSize());
        //构建查询条件
        QueryWrapper<Blogs> wrapper = new QueryWrapper<>();
        wrapper.select()
                .and(i->i.like("category",query.getCategory()))
                .and(i->i.like("tag",query.getTags()))
                .and(i->i.like("title",query.getKeyword()))
                .and(i->i.eq("status",query.getStatus()));
        return selectPage(page,wrapper);
    }
}

