package com.example.service;

import com.example.common.enums.QuestionTypeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Question;
import com.example.entity.QuestionItem;
import com.example.mapper.QuestionMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 题目表业务处理
 **/
@Service
public class QuestionService {

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    QuestionItemService questionItemService;
    /**
     * 新增
     */
    public void add(Question question) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            question.setUserId(currentUser.getId());
        }
        questionMapper.insert(question);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        questionMapper.deleteById(id);
        //删除题目选项
        questionItemService.deleteByQuestionId(id);;
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Question question) {
        questionMapper.updateById(question);
    }

    /**
     * 根据ID查询
     */
    public Question selectById(Integer id) {
        return questionMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Question> selectAll(Question question) {
        return questionMapper.selectAll(question);
    }

    /**
     * 分页查询
     */
    public PageInfo<Question> selectPage(Question question, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Question> list = questionMapper.selectAll(question);
        return PageInfo.of(list);
    }

    public List<Question> selectByPageId(Integer pageId) {
        List<Question> questionList = questionMapper.selectByPageId(pageId);
        for(Question question :questionList){
            List<QuestionItem> questionItemList = questionItemService.selectByQuestionId(question.getId());
            question.setQuestionItemList(questionItemList);
        }

        return questionList;
    }

    //用户新建题目
    @Transactional
    public void addForUser(Question question) {
        this.add(question);
        Integer questionId = question.getId();
        //单选和多选需要默认两个选项
        if(QuestionTypeEnum.SINGLE.getValue().equals(question.getType())||QuestionTypeEnum.MULTPLE.getValue().equals(question.getType())){
            QuestionItem questionItem1 = new QuestionItem();
            questionItem1.setQuestionId(questionId);
            questionItemService.add(questionItem1);

            QuestionItem questionItem2 = new QuestionItem();
            questionItem2.setQuestionId(questionId);
            questionItemService.add(questionItem2);

        }
    }

    public void deleteByPageId(Integer pageId) {
        List<Question> questionList = this.selectByPageId(pageId);
        // 根据问卷ID删除题目
        questionMapper.deleteByPageId(pageId);
        // 删除题目选项
        for (Question question : questionList) {
            questionItemService.deleteByQuestionId(question.getId());
        }

    }
}