//日期格式化
export default {
    dateFormat(datetime) {
        let year = datetime.getFullYear();
        let month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
        let date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
        let hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
        let minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
        let second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
        return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
    }
}

