import request from "@/utils/request";

export default {
  //根据题解id获取题解的全部评论
  queryAllCommentById(data) {
    return request({
      url: "/comment/queryAllCommentById",
      method: 'POST',
      data
    })
  },
  deleteCommentById(id){
    return request({
      url: "/comment/deleteCommentById?id="+id,
      method:"GET"
    })
  }
}
