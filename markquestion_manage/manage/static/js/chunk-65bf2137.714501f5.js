(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-65bf2137"],{"1d90":function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.adviceTagList,border:""}},[a("el-table-column",{attrs:{fixed:"",prop:"id",label:"id"}}),a("el-table-column",{attrs:{prop:"adviceTagName",label:"名称"}}),a("el-table-column",{attrs:{prop:"createTime",label:"创建时间"}}),a("el-table-column",{attrs:{prop:"adviceTagNum",label:"标签下的推荐数量"}}),a("el-table-column",{attrs:{fixed:"right",label:"操作",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{staticStyle:{color:"red"},attrs:{type:"text",size:"small"},on:{click:function(a){return e.deleteAdviceTag(t.row.id)}}},[e._v("删除")])]}}])})],1)],1)},c=[],n=(a("d3b7"),a("159b"),a("8039")),d=a("fb92"),r={name:"adviceTagList",data:function(){return{adviceTagList:[]}},mounted:function(){this.getAdviceTagList()},methods:{getAdviceTagList:function(){var e=this;n["a"].getAdviceTagList().then((function(t){e.adviceTagList=t.data,e.adviceTagList.forEach((function(e){e.createTime=d["a"].dateFormat(new Date(e.createTime))}))}))},deleteAdviceTag:function(e){var t=this;this.$confirm("是否要删除当前标签(删除将无法找回)?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){n["a"].deleteAdviceTag(e).then((function(e){200===e.code?t.$message.success({message:"删除成功",duration:1e3}):t.$message.error({message:"删除失败",duration:1e3}),t.getAdviceTagList()})).catch((function(e){t.$message.error({message:"网络出错啦"+e,duration:1e3})}))})).catch((function(){t.$message({type:"info",message:"已取消删除"})}))}}},u=r,o=a("2877"),s=Object(o["a"])(u,i,c,!1,null,"3c907447",null);t["default"]=s.exports},8039:function(e,t,a){"use strict";var i=a("b775");t["a"]={queryAllAdvice:function(e){return Object(i["a"])({url:"/advice/queryAllAdvice",method:"POST",data:e})},getAdviceTagList:function(){return Object(i["a"])({url:"/advice/getAdviceTagList",method:"GET"})},deleteImg:function(e){return Object(i["a"])({url:"/advice/deleteImg?name="+e,method:"GET"})},addTag:function(e){return Object(i["a"])({url:"/advice/addTag?tagName="+e,method:"GET"})},addAdvice:function(e){return Object(i["a"])({url:"/advice/addAdvice",method:"POST",data:e})},deleteAdvice:function(e){return Object(i["a"])({url:"/advice/deleteAdvice?id="+e,method:"GET"})},updateAdvice:function(e){return Object(i["a"])({url:"/advice/updateAdvice",method:"POST",data:e})},deleteAdviceTag:function(e){return Object(i["a"])({url:"/advice/deleteAdviceTag?id="+e,method:"GET"})}}},fb92:function(e,t,a){"use strict";t["a"]={dateFormat:function(e){var t=e.getFullYear(),a=e.getMonth()+1<10?"0"+(e.getMonth()+1):e.getMonth()+1,i=e.getDate()<10?"0"+e.getDate():e.getDate(),c=e.getHours()<10?"0"+e.getHours():e.getHours(),n=e.getMinutes()<10?"0"+e.getMinutes():e.getMinutes(),d=e.getSeconds()<10?"0"+e.getSeconds():e.getSeconds();return t+"-"+a+"-"+i+" "+c+":"+n+":"+d}}}}]);