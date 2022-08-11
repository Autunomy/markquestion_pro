<template>
    <div>
        <div>
            <mavon-editor
                @imgAdd="imgAdd"
                @imgDel="imgDel"
                :fontSize="'20'"
                style="min-height: 600px;"
                ref=md
                :tab-size="4"></mavon-editor>
        </div>
    </div>
</template>

<script>
import blogApi from "@/api/blog";

export default {
    name: "index",
    data() {
        return{

        }
    },
    methods: {
        // 图片上传
        imgAdd(pos, $file) {
            // 第一步.将图片上传到服务器.
            let data = new FormData();
            data.append('image', $file);
            blogApi.uploadPic(data).then(resp => {
                //设置图片地址
                this.$refs.md.$img2Url(pos,resp.data)
            })
        },
        //上传图片删除
        imgDel(pos,$file){
            console.log(pos,$file)
        }
    }
}
</script>

<style scoped>

</style>