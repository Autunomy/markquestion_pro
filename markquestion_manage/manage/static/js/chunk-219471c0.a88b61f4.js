(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-219471c0"],{"1ddd":function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-form",{staticStyle:{width:"600px",margin:"30px auto"},attrs:{"label-position":"right","label-width":"100px",model:e.advice}},[a("el-form-item",{attrs:{label:"推荐名称"}},[a("el-input",{model:{value:e.advice.adviceName,callback:function(t){e.$set(e.advice,"adviceName",t)},expression:"advice.adviceName"}})],1),a("el-form-item",{attrs:{label:"推荐作者"}},[a("el-input",{model:{value:e.advice.author,callback:function(t){e.$set(e.advice,"author",t)},expression:"advice.author"}})],1),a("el-form-item",{attrs:{label:"推荐标签"}},[a("el-select",{attrs:{placeholder:"请选择"},model:{value:e.advice.adviceTag,callback:function(t){e.$set(e.advice,"adviceTag",t)},expression:"advice.adviceTag"}},e._l(e.adviceTagList,(function(e){return a("el-option",{key:e.adviceTagName,attrs:{label:e.adviceTagName,value:e.adviceTagName}})})),1),a("el-button",{staticStyle:{"margin-left":"20px"},on:{click:e.showDialog}},[e._v("添加标签")]),a("el-button",{staticStyle:{"margin-left":"20px"},on:{click:e.manageTag}},[e._v("管理标签")])],1),a("el-form-item",{attrs:{label:"推荐简介"}},[a("el-input",{attrs:{placeholder:"简短的介绍即可"},model:{value:e.advice.adviceDescription,callback:function(t){e.$set(e.advice,"adviceDescription",t)},expression:"advice.adviceDescription"}})],1),a("el-form-item",{attrs:{label:"推荐描述"}},[a("el-input",{attrs:{type:"textarea",placeholder:"支持markdown语法"},model:{value:e.advice.adviceContent,callback:function(t){e.$set(e.advice,"adviceContent",t)},expression:"advice.adviceContent"}})],1),a("el-form-item",{attrs:{label:"上传图片"}},[a("el-upload",{staticClass:"avatar-uploader",attrs:{action:"http://180.76.97.59:8001/advice/uploadImg","show-file-list":!1,"on-success":e.handleAvatarSuccess}},[e.imageUrl?a("img",{staticClass:"avatar",attrs:{src:e.imageUrl}}):a("i",{staticClass:"el-icon-plus avatar-uploader-icon",staticStyle:{border:"1px solid gray"}})])],1),a("el-form-item",{staticStyle:{"text-align":"center"}},[a("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.addAdvice()}}},[e._v("提交")]),a("el-button",{on:{click:function(t){return e.resetForm()}}},[e._v("重置")])],1)],1),a("el-dialog",{attrs:{title:"添加标签",visible:e.isShow},on:{"update:visible":function(t){e.isShow=t}}},[a("el-form",{attrs:{model:e.newTag}},[a("el-form-item",{attrs:{label:"标签名称"}},[a("el-input",{attrs:{autocomplete:"off"},model:{value:e.newTag.name,callback:function(t){e.$set(e.newTag,"name",t)},expression:"newTag.name"}})],1)],1),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.isShow=!1}}},[e._v("取 消")]),a("el-button",{attrs:{type:"primary"},on:{click:e.addTag}},[e._v("确 定")])],1)],1)],1)},c=[],n=(a("b0c0"),a("8039")),d=a("4328"),o=a.n(d),l={name:"add",data:function(){return{advice:{},adviceTagList:[],isShow:!1,newTag:{},imageUrl:""}},mounted:function(){this.getAdviceTagList()},methods:{getAdviceTagList:function(){var e=this;n["a"].getAdviceTagList().then((function(t){e.adviceTagList=t.data}))},addTag:function(){var e=this;n["a"].addTag(this.newTag.name).then((function(t){console.log(t),200===t.code?(e.$message.success({message:"添加成功",duration:1500}),e.getAdviceTagList()):e.$message.error({message:"添加失败",duration:1500})})),this.isShow=!1},addAdvice:function(){var e=this;console.log(o.a.stringify(this.advice)),n["a"].addAdvice(o.a.stringify(this.advice)).then((function(t){200===t.code?(e.$message.success({message:"添加成功",duration:1500}),e.$router.push("/advice/list")):(e.$message.error({message:"添加失败",duration:1500}),e.resetForm())}))},resetForm:function(){this.deleteImg(),this.advice={}},showDialog:function(){this.isShow=!0},manageTag:function(){},handleAvatarSuccess:function(e){this.imageUrl="http://180.76.97.59:8001/images/advice/"+e.data,this.advice.adviceImg=e.data},deleteImg:function(){n["a"].deleteImg(this.advice.adviceImg),this.imageUrl=""}}},s=l,r=(a("e4f0"),a("2877")),u=Object(r["a"])(s,i,c,!1,null,"c68ca5a4",null);t["default"]=u.exports},"34bc":function(e,t,a){},8039:function(e,t,a){"use strict";var i=a("b775");t["a"]={queryAllAdvice:function(e){return Object(i["a"])({url:"/advice/queryAllAdvice",method:"POST",data:e})},getAdviceTagList:function(){return Object(i["a"])({url:"/advice/getAdviceTagList",method:"GET"})},deleteImg:function(e){return Object(i["a"])({url:"/advice/deleteImg?name="+e,method:"GET"})},addTag:function(e){return Object(i["a"])({url:"/advice/addTag?tagName="+e,method:"GET"})},addAdvice:function(e){return Object(i["a"])({url:"/advice/addAdvice",method:"POST",data:e})},deleteAdvice:function(e){return Object(i["a"])({url:"/advice/deleteAdvice?id="+e,method:"GET"})},updateAdvice:function(e){return Object(i["a"])({url:"/advice/updateAdvice",method:"POST",data:e})},deleteAdviceTag:function(e){return Object(i["a"])({url:"/advice/deleteAdviceTag?id="+e,method:"GET"})},searchAdvice:function(e){return Object(i["a"])({url:"/advice/searchAdvice?"+e,method:"GET"})}}},e4f0:function(e,t,a){"use strict";a("34bc")}}]);