import axios from 'axios'

const service = axios.create({
    baseURL: "http://120.48.149.8:8001",
    timeout: 5000 // 超时限制
})

// // 请求拦截器
// service.interceptors.request.use(
//     config => {
//         if(getToken()){
//             //已经登陆过了
//             config.headers['Authenticate'] = getToken()
//         }
//         return config
//     }
// )
//
// // 响应拦截器
// service.interceptors.response.use(
//     response => {
//         //更新令牌 续约token
//         setToken(response.headers.authenticate)
//         //响应实体
//         return response.data
//     }
// )

export default service
