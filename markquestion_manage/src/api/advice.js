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
  }
}
