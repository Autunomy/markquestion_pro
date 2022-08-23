import request from '@/utils/request'

export default {
  queryQuestionPage(data) {
    return request({
      url: "/question/queryQuestionPage?" + data,
      method: "GET"
    })
  },

  getQuestionFrom() {
    return request({
      url: "/question/getQuestionFrom",
      method: 'GET'
    })
  },
  addQuestion(data) {
    return request({
      url: "/question/addQuestion",
      method: "POST",
      data
    })
  },
  queryQuestionById(id){
    return request({
      url:"/question/queryQuestionById?qid="+id,
      method:"GET"
    })
  },
  updateQuestion(data){
    return request({
      url:"/question/updateQuestion",
      method:"POST",
      data
    })
  },
  deleteQuestionById(id){
    return request({
      url:"/question/deleteQuestionById?id="+id,
      method:"GET"
    })
  },
  searchQuestion(search) {
    return request({
      url:"/question/searchQuestion?"+search,
      method:"GET"
    })
  }
}
