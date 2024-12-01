<script>
export default {
  name: "Preview",
  data(){
    return {
      pages:{},
      questionList:{}
    }
  },
  created() {
    this.load()
  },
  methods:{
    closeWin(){
      window.close()
    },
    load(){
      let pageId= this.$route.query.pageId
      this.$request.get('/pages/selectById/'+pageId).then(res=>{
        this.pages=res.data||{}
      })
      this.$request.get('question/selectByPageId/',{
        params:{
          pageId:pageId
        }
      }).then(res=>{
        this.questionList=res.data||[]
      })
    }
  }
}
</script>

<template>
  <div class="main-content">
    <div class="card">
      <div style="text-align: center;padding-bottom: 20px;margin-bottom:20px;border-bottom: 1px solid #ddd;font-size: 24px;color:#2a60c9;font-weight: bold">{{pages.name}}</div>
      <div style="padding: 10px 0">
          <div v-for="(item,index) in questionList" :key="item.id"style="margin-bottom: 15px">
            <div style="margin-right: 10px;font-size: 18px "><span>{{index+1}}.</span>
              <span style="margin-right: 10px">{{item.name}}</span>
              <el-tag v-if="item.type==='单选题'" type="primary">单选题</el-tag>
              <el-tag v-if="item.type==='多选题'" type="success">多选题</el-tag>
              <el-tag v-if="item.type==='填空题'" type="warning">填空题</el-tag>
            </div>
            <div style="padding-left: 20px">
              <div v-for="sub in item.questionItemList" :key="sub.id"style="margin-top: 5px">
                <div v-if="item.type==='单选题'">
                <el-radio :label="sub.content"></el-radio>
                </div>
                <div v-if="item.type==='多选题'">
                <el-checkbox :label="sub.content"></el-checkbox>
                </div>
              </div>
              <div v-if="item.type==='填空题'"style="margin-top: 5px">
                <el-input type="textarea"></el-input>
              </div>
            </div>
          </div>
      </div>
      <div style="text-align: center">
        <el-button size="medium"type="primary" @click="closeWin()">关 闭</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>