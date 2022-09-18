<template>
    <div>
        <h1>asd</h1>
    </div>
</template>

<script>
export default {
    name: "blogContent",
    data(){
        return {
            id:""
        }
    },
    mounted() {
        this.queryBlogById();
    },
    methods:{
        queryBlogById(){
            this.id = this.$route.params.id;
            console.log(this.id)
        }
    }
}
</script>

<style scoped>

</style>