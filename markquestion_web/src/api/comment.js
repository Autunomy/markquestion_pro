import request from "@/utils/request";

export default {
    //保存评论
    saveComment(data) {
        return request({
            url:"/comment/saveComment",
            method:"POST",
            data
        })
    },
    //获取非回复评论
    queryAllCommentByQuestionId(qid){
        return request({
            url:"/comment/queryAllCommentByQuestionId?qid="+qid,
            method:"GET"
        })
    },
    //获取当前评论的所有回复评论
    queryReplyCommentById(id){
        return request({
            url:"/comment/queryReplyCommentById?id="+id,
            method:"GET"
        })
    }
}