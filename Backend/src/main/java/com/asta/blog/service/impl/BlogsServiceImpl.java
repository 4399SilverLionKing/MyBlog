package com.asta.blog.service.impl;

import com.asta.blog.entity.Blogs;
import com.asta.blog.mapper.BlogsMapper;
import com.asta.blog.service.IBlogsService;
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

}
