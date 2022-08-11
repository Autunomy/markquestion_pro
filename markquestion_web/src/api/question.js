import request from "@/utils/request";

export default {
    //分页查询
    queryQuestionPage(data) {
        return request({
            url: "/question/queryQuestionPage?" + data,
            method: 'GET'
        })
    },
    //根据id查
    queryQuestionById(qid) {
        return request({
            url: "/question/queryQuestionById?qid=" + qid,
            method: "GET"
        })
    },
    //根据作者分页查
    queryQuestionPageByAuthor(data) {
        return request({
            url: "/question/queryQuestionPageByAuthor",
            method: "POST",
            data
        })
    },
    //题解浏览量+1
    addWatch(id) {
        return request({
            url: "/question/addWatch?id=" + id,
            method: "GET"
        })
    },
    //搜索题解 按照 id  题目名称 题目来源  作者查询
    searchQuestion(search) {
        return request({
            url:"/question/searchQuestion?"+search,
            method:"GET"
        })
    }
}