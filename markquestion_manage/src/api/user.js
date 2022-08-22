import request from '@/utils/request'

export default {
  login(data){
    return request({
      url: '/user/login',
      method: 'post',
      data
    })
  },
  getInfo(token){
    //这里不需要在请求头中携带token，因为在请求拦截器中已经自动进行配置了
    return request({
      url: '/user/userInfo',
      method: 'get'
    })
  },
  addUser(data){
    return request({
      url: '/rbac/user/add',
      method: 'POST',
      data
    })
  },
  getUserList(data){
    return request({
      url: '/rbac/user/list',
      method: 'POST',
      data
    })
  },
  deleteUser(id){
    return request({
      url:`/rbac/user/delete?id=${id}`,
      method:"GET",
    })
  },
  updateUserById(id,data){
    return request({
      url:`/rbac/user/update?id=${id}`,
      method:"POST",
      data
    })
  },

}
