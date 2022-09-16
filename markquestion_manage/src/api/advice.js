import request from "@/utils/request";

export default {
  queryAllAdvice(data) {
    return request({
      url: "/advice/queryAllAdvice",
      method: 'POST',
      data
    })
  },
  getAdviceTagList(){
    return request({
      url:"/advice/getAdviceTagList",
      method:"GET"
    })
  },
  deleteImg(name){
    return request({
      url:"/advice/deleteImg?name="+name,
      method:"GET"
    })
  },
  addTag(tagName){
    return request({
      url:"/advice/addTag?tagName="+tagName,
      method:"GET"
    })
  },
  addAdvice(data){
    return request({
      url:"/advice/addAdvice",
      method:"POST",
      data
    })
  },
  deleteAdvice(id){
    return request({
      url:"/advice/deleteAdvice?id="+id,
      method:"GET"
    })
  },
  updateAdvice(data){
    return request({
      url:"/advice/updateAdvice",
      method:"POST",
      data
    })
  },
  deleteAdviceTag(id){
    return request({
      url:"/advice/deleteAdviceTag?id="+id,
      method:"GET"
    })
  }

}
