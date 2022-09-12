import request from "@/utils/request";

export default {
  queryAllLink(data) {
    return request({
      url: "/friendLink/queryFriendLinkList",
      method: 'POST',
      data
    })
  },
  addFriendLink(data){
    return request({
      url:"/friendLink/addFriendLink",
      method:"POST",
      data
    })
  },
  queryFriendLinkTag(){
    return request({
      url:"/friendLink/queryFriendLinkTag",
      method:"GET"
    })
  },
  addFriendLinkTag(data){
    return request({
      url:"/friendLink/addFriendLinkTag",
      method:"POST",
      data
    })
  },
  searchFriendLinkByName(name){
    return request({
      url:"/friendLink/searchFriendLinkByName?name="+name,
      method:"GET"
    })
  }
}

