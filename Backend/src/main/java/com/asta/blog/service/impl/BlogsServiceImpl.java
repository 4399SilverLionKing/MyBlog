package com.asta.blog.service.impl;

import com.asta.blog.mapper.MsBlogsMapper;
import com.asta.blog.models.dto.PageDTO;
import com.asta.blog.models.entity.Blogs;
import com.asta.blog.mapper.BlogsMapper;
import com.asta.blog.models.query.BlogListQuery;
import com.asta.blog.models.dto.PostBlogDTO;
import com.asta.blog.models.dto.PutBlogDTO;
import com.asta.blog.models.vo.GetBlogContentVO;
import com.asta.blog.models.vo.PostBlogVO;
import com.asta.blog.service.IBlogsService;
import com.asta.blog.models.vo.BlogListVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private static final Logger logger = LoggerFactory.getLogger(BlogsServiceImpl.class);

    @Value("${qiniu.accessKey}")
    String accessKey;

    @Value("${qiniu.secretKey}")
    String secretKey;

    @Value("${qiniu.bucket}")
    String bucket;

    @Value("${qiniu.domain}")
    String domain;

    @Autowired
    MsBlogsMapper ms;

    @Override
    public PageDTO<BlogListVO> getBlogList(BlogListQuery query) {
        Page<Blogs> blogsPage = baseMapper.SelectByQuery(query);
        return PageDTO.create(blogsPage,ms::toBlogListVO);
    }

    @SneakyThrows
    @Override
    public GetBlogContentVO getBlogContent(String key) {
        // domain   下载 domain, eg: qiniu.com【必须】
        // useHttps 是否使用 https【必须】
        // key      下载资源在七牛云存储的 key【必须】
        DownloadUrl url = new DownloadUrl(domain, false, key);
        // 带有效期
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        long deadline = System.currentTimeMillis()/1000 + expireInSeconds;
        Auth auth = Auth.create(accessKey, secretKey);
        GetBlogContentVO vo = new GetBlogContentVO();
        vo.setUrl(url.buildURL(auth,deadline));
        vo.setExpires(deadline);
        return vo;
    }

    @Override
    public PostBlogVO PostBlog(PostBlogDTO dto) {
        Blogs blog = ms.PostDTOtoBlog(dto);
        blog.setDate(LocalDateTime.now());
        blog.setReadCount(0);
        baseMapper.insert(blog);
        Integer id = blog.getId();
        Auth auth = Auth.create(accessKey, secretKey);
        PostBlogVO postBlogVO = new PostBlogVO();
        postBlogVO.setId(id);
        postBlogVO.setToken(auth.uploadToken(bucket));
        return postBlogVO;
    }

    @Override
    public String PutBlog(PutBlogDTO dto) {
        Blogs blog = ms.PutDTOtoBlog(dto);
        blog.setDate(LocalDateTime.now());
        baseMapper.updateById(blog);
        Auth auth = Auth.create(accessKey, secretKey);
        String key = String.format("%s.md", dto.getId());
        return auth.uploadToken(bucket,key);
    }

    @Override
    public void DeleteBlog(Integer id) {
        Configuration cfg = new Configuration(Region.qvmHuadong());
        Auth auth = Auth.create(accessKey, secretKey);
        String key = String.format("%s.md", id);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
        baseMapper.deleteById(id);
    }

    @Override
    public void updateBlogReadCount(Integer id) {
        Blogs blog = baseMapper.selectById(id);
        if (blog != null) {
            blog.setReadCount(blog.getReadCount() + 1);
            baseMapper.updateById(blog);
            logger.info("更新博客 ID: {} 的阅读量为: {}", id, blog.getReadCount());
        } else {
            logger.warn("尝试更新不存在的博客阅读量，ID: {}", id);
        }
    }
}
