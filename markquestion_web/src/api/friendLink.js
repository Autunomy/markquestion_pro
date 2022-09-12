import request from "@/utils/request";

export default {
    queryFriendLinkList(){
        return request({
            url:"/friendLink/getFriendLink",
            type:"GET"
        })
    }
}