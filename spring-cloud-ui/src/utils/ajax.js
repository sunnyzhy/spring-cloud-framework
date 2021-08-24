import axios from 'axios'
// import {
//   Message,
//   MessageBox
// } from 'element-ui'
import store from '../store'
import {
  getToken
} from '@/utils/token'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_API,
  timeout: 30000
})

// request拦截器
service.interceptors.request.use(config => {
  console.log(store.getters.token.token)
  if (store.getters.token) {
    config.headers['access-token'] = getToken()
  }
  return config
}, error => {
  console.log(error) // for debug
  Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(
  response => {
    const resp = response.data
    if (resp.code !== 200) {
      return Promise.reject(resp.msg)
    }
    return resp.data
  },
  error => {
    return Promise.reject(error)
  }
)
export default service
