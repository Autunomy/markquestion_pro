import request from "@/utils/request";

export default {
  queryContestPage(data) {
    return request({
      url: "/contest/queryContestPage",
      method: 'POST',
      data
    })
  },
  deleteContestById(id) {
    return request({
      url:"/contest/deleteContestById?id="+id,
      method:"GET"
    })
  },
  addContest(data){
    return request({
      url:"/contest/addContest",
      method:"POST",
      data
    })
  },
  updateContest(data){
    return request({
      url:"/contest/updateContest",
      method:"POST",
      data
    })
  }
}

