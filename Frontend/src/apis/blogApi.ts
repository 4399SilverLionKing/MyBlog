import { del, get, post, put } from './http'

// 定义博客接口类型
export interface BlogApiInterface {
  id?: number
  title?: string
  subtitle?: string
  category?: string
  date?: string
  readCount?: number
  tags?: string[]
  status?: string // 0: 草稿, 1: 已发布
  content?: string // Markdown文件内容
}

// 获取博客列表的参数接口
export interface BlogListParams {
  pageIndex: number
  pageSize: number
  category?: string
  tag?: string
  keyword?: string
  status?: number
}

// 定义响应数据的通用结构
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

// 定义分页数据的通用结构
export interface PageResponse<T> {
  pageIndex: number
  pageSize: number
  rows: T[]
}

// 定义新建博客返回结果
export interface NewBlogResponse {
  id: number
  token: string
}

/**
 * 获取博客列表
 */
export function getBlogList(params: BlogListParams) {
  return get<ApiResponse<PageResponse<BlogApiInterface>>>('/public/blogs', params)
}

/**
 * 更新博客阅读量（公共接口，无需权限认证）
 */
export function updateBlogReadCount(id: number) {
  return post<ApiResponse<null>>(`/public/blogs/read-count/${id}`)
}

/**
 * 创建博客
 */
export function createBlog(blog: BlogApiInterface) {
  return post<ApiResponse<NewBlogResponse>>('/blogs', blog)
}

/**
 * 更新博客
 */
export function putBlog(blog: BlogApiInterface) {
  return put<ApiResponse<string>>('/blogs', blog)
}

/**
 * 删除博客
 */
export function deleteBlog(id: number | string) {
  return del<ApiResponse<null>>(`/blogs/${id}`)
}
