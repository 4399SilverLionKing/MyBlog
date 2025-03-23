import type { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import axios from 'axios'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8',
  },
})

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 可以在这里添加token等认证信息
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('请求错误：', error)
    return Promise.reject(error)
  },
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response

    // 这里可以根据后端返回的状态码进行不同的处理
    if (data.code && data.code !== 200) {
      console.error('接口请求失败：', data.message || '未知错误')
      return Promise.reject(new Error(data.message || '未知错误'))
    }

    return data
  },
  (error) => {
    console.error('响应错误：', error)
    return Promise.reject(error)
  },
)

// 封装GET请求
export function get<T>(url: string, params?: any, config = {}): Promise<T> {
  return service.get(url, { params, ...config })
}

// 封装POST请求
export function post<T>(url: string, data?: any, config = {}): Promise<T> {
  return service.post(url, data, config)
}

// 封装PUT请求
export function put<T>(url: string, data?: any, config = {}): Promise<T> {
  return service.put(url, data, config)
}

// 封装DELETE请求
export function del<T>(url: string, config = {}): Promise<T> {
  return service.delete(url, config)
}

export default service
