import request from "@/utils/request";

export default {
  queryAllLink(data) {
    return request({
      url: "/friendLink/queryFriendLinkList",
      method: 'POST',
      data
    })
  },
}

