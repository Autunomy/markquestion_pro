import request from "@/utils/request";

export default {
  getMessageBoard(data) {
    return request({
      url: "/messageBoard/queryMessageBoard",
      method: 'POST',
      data
    })
  },
  deleteMessage(id){
    return request({
      url: "/messageBoard/deleteMessage?id="+id,
      method: 'GET'
    })
  },
  searchMessage(data){
    return request({
      url: "/messageBoard/searchMessage",
      method: 'POST',
      data
    })
  }
}
