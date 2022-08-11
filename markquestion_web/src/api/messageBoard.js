import request from "@/utils/request";

export default {
    //分页查询日期最近的5条留言
    queryMessageBoard(data){
        return request({
            url:"/messageBoard/queryMessageBoard",
            method:"POST",
            data
        })
    },
    //向服务器上传留言
    saveMessage(data){
        return request({
            url:"/messageBoard/saveMessage",
            method:"POST",
            data
        })
    }

}