import request from "@/utils/request";

export default {
  //图片上传
  uploadPic(data) {
    return request({
      url: "/blog/uploadPic",
      method: 'POST',
      data,
      headers: {'Content-Type': 'multipart/form-data'}
    })
  },
  delPic(path) {
    return request({
      url: "/blog/delPic?path=" + path,
      method: 'GET'
    })
  },
  queryAllBlog(data){
    return request({
      url: "/blog/queryAllBlog",
      method: 'POST',
      data
    })
  },
  queryAllClass(){
    return request({
      url:"/blog/queryAllClass",
      method:"GET"
    })
  }
}
