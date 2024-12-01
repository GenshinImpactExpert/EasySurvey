<script>
export default {
  name: "Design",
  data(){
    return {
      pages:{},
      questionList:[]
    }
  },
  created(){
    this.load()
  },
  methods:{
    handleSaved() {
      if (!this.validate()) { // 校验没通过  不能发布
        return
      }
      this.$confirm('您确认发布吗？', '确认发布', {type: "warning"}).then(response => {
        let data = JSON.parse(JSON.stringify(this.pages))
        data.saved = '是'
        this.$request.put('/pages/update/', data).then(res => {
          if (res.code === '200') {  // 表示成功保存
            this.$message.success('操作成功')
            this.load()
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(err => console.log(err))
    },
    validate() {
      let flag = false  // 检查内容是否为空  当flag = true 的时候就是空的内容
      for (let i = 0; i < this.questionList.length; i++) {
        if (this.questionList[i].type === '单选题' || this.questionList[i].type === '多选题') {
          if (!this.questionList[i].questionItemList?.length) {
            this.$message.warning('单选题和多选题需要添加选项')
            return false
          }
          this.questionList[i].questionItemList.forEach(item => {
            if (!item.content) {
              flag = true
            }
          })
        }
        if (!this.questionList[i].name) {
          this.$message.warning('请填写题目名称')
          return false
        }
        if (flag) {
          this.$message.warning('请完善选项内容')
          return false
        }
      }
      return true
    },
    preview(pageId){
      if(this.validate()){//校验通过
        window.open('/front/preview?pageId=' + pageId);
      }

    },
    addQuestionItem(question){
      this.$request.post('/questionItem/add',{questionId: question.id, content: question.newContent}).then(res=>{
        if(res.code==='200'){
          this.$message.success('操作成功')
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    addQuestion(type){
      this.$request.post('/question/addForUser',{name:'',type:type,pageId:this.pages.id}).then(res=>{
        if(res.code==='200'){
          this.$message.success('操作成功')
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    changeQuestionName(question){
      let data=JSON.parse(JSON.stringify(question))
      this.$request.put('/question/update',data).then(res=>{
        if(res.code==='200'){
          this.$message.success('更新成功')
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    changeQuestionItem(questionItem){
      let data=JSON.parse(JSON.stringify(questionItem))
      this.$request.put('/questionItem/update',data).then(res=>{
        if(res.code==='200'){
          this.$message.success('更新成功')
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    delQuestion(questionId) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/question/delete/' + questionId).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    delQuestionItem(questionItemId) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/questionItem/delete/' + questionItemId).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
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
      <div style="display: flex;margin-bottom: 20px" >
        <div style="flex:1">
         <div  > <span style="font-size: 24px;margin-right: 10px">{{pages.name}}</span>
           <el-tag v-if="pages.saved==='否'">未发布</el-tag>
           <el-tag v-else type="success">已发布</el-tag>
         </div>
        </div>
        <div >
          <el-button type="primary"><i class="el-icon-share"></i>分享</el-button>
          <el-button type="info" @click="preview(pages.id)"><i class="el-icon-video-play" ></i>预览</el-button>
          <el-button type="success" :disabled="pages.saved==='是'" @click="handleSaved"><i class="el-icon-s-promotion"></i>发布</el-button>
        </div>
      </div>
      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="addQuestion('单选题')">新建单选题</el-button>
        <el-button type="info" @click="addQuestion('多选题')">新建多选题</el-button>
        <el-button type="success" @click="addQuestion('填空题')">新建填空题</el-button>
      </div>

      <div>
        <div v-for="(item,index) in questionList" :key="item.id" style="margin-bottom: 20px">
          <div>
            <span style="font-weight: bold;margin-right: 5px" >题目{{index+1}}</span>
            <el-input v-model="item.name" style="width: 70%;margin-right: 5px" @change="changeQuestionName(item)"></el-input>
            <el-tag style="margin-right: 5px;color:#2a60c9" v-if="item.type==='单选题'">{{item.type}}</el-tag>
            <el-tag style="margin-right: 5px;color:orange" v-if="item.type==='多选题'">{{item.type}}</el-tag>
            <el-tag style="margin-right: 5px;color:seagreen" v-if="item.type==='填空题'">{{item.type}}</el-tag>
            <span style="color:red;cursor:pointer;font-size: 12px;" @click="delQuestion(item.id)"><i class="el-icon-delete"></i>删除</span>
          </div>
          <div style="padding-left: 40px" v-if="item.questionItemList?.length">
            <div v-for="(sub,subIndex) in item.questionItemList" :key="sub.id" style="margin: 10px 0">
              <div>
                <span style="margin-right: 5px" >选项{{subIndex+1}}</span>
                <el-input v-model="sub.content" style="width: 50%;margin-right: 5px" @change="changeQuestionItem(sub)"></el-input>
                <span style="color:red;cursor:pointer;font-size: 12px;" @click="delQuestionItem(sub.id)"><i class="el-icon-delete"></i>删除</span>


              </div>

            </div>
            <div>
              <span style="margin-right: 5px;color:#2a609c;" >新选项</span>
              <el-input v-model="item.newContent" style="width: 50%;margin-right: 5px"></el-input>
              <el-button type="primary" class="btn-green" size="mini" @click="addQuestionItem(item)">确认添加</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>