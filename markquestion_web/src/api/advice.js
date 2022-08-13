import request from "@/utils/request";

export default {
    queryAdviceList(){
        return request({
            url:"/advice/queryAdviceList",
            type:"GET"
        })
    }
}