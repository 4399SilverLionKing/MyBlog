import type { ApiResponse } from './blogApi'
import { ElMessage } from 'element-plus'
import * as qiniu from 'qiniu-js'
import { get } from './http'

/**
 * 七牛云存储服务相关API
 */

/**
 * 签名下载地址接口
 */
export interface SignedUrlResponse {
  url: string
  expires: number
}

/**
 * 上传凭证接口
 */
export interface UploadTokenResponse {
  token: string
  key: string
}

/**
 * 获取带签名的七牛云私有存储下载链接
 * @param key 文件在七牛云存储中的键名
 * @returns 带签名的下载地址和过期时间
 */
export function getSignedDownloadUrl(key: string) {
  return get<ApiResponse<SignedUrlResponse>>(`/public/blogs/${key}`)
}

/**
 * 更新博客内容到七牛云
 * @param blogId 博客ID
 * @param content 博客内容
 * @returns 上传结果
 */
export function updateBlogContent(blogId: number, content: string, token: string) {
  // 博客内容存储的key
  const key = `${blogId}.md`
  // 将字符串内容转换为Blob对象，然后创建File对象
  const blob = new Blob([content], { type: 'text/markdown' })
  const file = new File([blob], `${key}`, { type: 'text/markdown' })
  const observable = qiniu.upload(file, key, token)
  const observer = {
    error() {
      ElMessage.error('博客内容更新失败')
    },
    complete() {
      ElMessage.success('博客内容更新成功')
    },
  }
  return observable.subscribe(observer)
}

/**
 * 上传博客内容到七牛云
 * @param blogId 博客id
 * @param content 博客内容
 * @param token 凭证
 * @returns 上传结果
 */
export function postBlogContent(blogId: number, content: string, token: string) {
  // 博客内容存储的key
  const key = `${blogId}.md`
  // 将字符串内容转换为Blob对象，然后创建File对象
  const blob = new Blob([content], { type: 'text/markdown' })
  const file = new File([blob], `${key}`, { type: 'text/markdown' })
  const observable = qiniu.upload(file, key, token)
  const observer = {
    error() {
      ElMessage.error('博客内容创建失败')
    },
    complete() {
      ElMessage.success('博客内容创建成功')
    },
  }
  return observable.subscribe(observer)
}
