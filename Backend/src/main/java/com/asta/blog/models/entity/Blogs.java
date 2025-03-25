package com.asta.blog.models.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>
 * 
 * </p>
 *
 * @author Asta
 * @since 2025-03-23
 */
@Getter
@Setter
@ToString
@TableName("blogs")
public class Blogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 博客id
     */
    @TableId("id")
    private Integer id;

    /**
     * 博客标题
     */
    @TableField("title")
    private String title;

    /**
     * 博客简介
     */
    @TableField("desc")
    private String desc;

    /**
     * 博客类别
     */
    @TableField("category")
    private String category;

    /**
     * 博客创建日期
     */
    @TableField("date")
    private LocalDateTime date;

    /**
     * 博客阅读数量
     */
    @TableField("read_count")
    private String readCount;

    /**
     * 博客标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 博客状态（0草稿、1发布）
     */
    @TableField("status")
    private String status;

    /**
     * 博客md文件云SDk
     */
    @TableField("content")
    private String content;
}
