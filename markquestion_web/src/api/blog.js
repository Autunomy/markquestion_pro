import request from "@/utils/request";

export default {
    //图片上传
    uploadPic(data){
        return request({
            url:"/question/uploadPic",
            method:'POST',
            data,
            headers:{'Content-Type': 'multipart/form-data'}
        })
    }
}