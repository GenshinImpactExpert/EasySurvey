<template>
  <div class="main-content">
    <el-row :gutter="10">
      <el-col :span="8" v-for="item in pagesList" :key="item.id">
        <div class="card" style="margin-bottom: 10px" >
         <div style="display: flex;grid-gap: 10px;margin-bottom: 10px">
           <div style="flex: 1;width: 0">
             <div style="margin-bottom: 10px;font-size: 18px" class="line1">{{item.name}}</div>
             <div style="color:#666" class="line1">{{item.descr}}</div>
           </div>
           <div style="width: 80px">
             <img :src="item.img" alt="" style="width: 80px;height: 53.33px">
           </div>
         </div>
          <div>
            <el-button icon="el-icon-video-play" type="success" size="mini" class="btn-purple" @click="preview(item.id)">预览</el-button>
            <el-button type="primary" size="mini" class="btn-green" @click="copy(item.id)">使用该模板</el-button>
          </div>
        </div>

      </el-col>

    </el-row>
  </div>
</template>

<script>

import preview from "@/views/front/Preview.vue";

export default {
  computed: {

  },

  data() {
    return {
        pagesList:[]
    }
  },
  mounted() {

    this.load()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    copy(pageId){
      this.$request.post('/pages/copy?pageId='+pageId).then(res=>{

        if(res.code==='200'){
          this.$message.success('复制成功！')
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    preview(pageId){
      window.open('/front/preview?pageId='+pageId)
    },
    load(){
      this.$request.get('/pages/selectAll',{params:{open:'是'}}).then(res=>{
        this.pagesList=res.data||[]
      })
    }
  }
}
</script>
