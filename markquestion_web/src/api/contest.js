import request from "@/utils/request";

export default {
    queryContestByDate(date){
        return request({
            url:"/contest/queryContestByDate?date="+date,
            method:"GET"
        })
    }
}