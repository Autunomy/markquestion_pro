import request from "@/utils/request";

export default {
    //分页查询
    queryQuestionPage(data){
        return request({
            url:"/question/queryQuestionPage?"+data,
            method:'GET'
        })
    },
    //根据id查
    queryQuestionById(qid){
        return request({
            url:"/question/queryQuestionById?qid="+qid,
            method:"GET"
        })
    }
}