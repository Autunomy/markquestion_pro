import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import {getToken, setToken} from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    if(getToken()){
      //已经登陆过了
      config.headers['Authenticate'] = getToken()
    }
    return config
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    //更新令牌 续约token
    setToken(response.headers.authenticate)
    //响应实体
    return response.data
  }
)

export default service
