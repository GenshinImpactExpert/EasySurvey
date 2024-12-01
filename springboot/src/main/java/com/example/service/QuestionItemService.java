package com.example.service;

import com.example.entity.QuestionItem;
import com.example.mapper.QuestionItemMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 题目内容表业务处理
 **/
@Service
public class QuestionItemService {

    @Resource
    private QuestionItemMapper questionItemMapper;

    /**
     * 新增
     */
    public void add(QuestionItem questionItem) {
        questionItemMapper.insert(questionItem);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        questionItemMapper.deleteById(id);
    }

    public void deleteByQuestionId(Integer questionId) {
        questionItemMapper.deleteByQuestionId(questionId);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            questionItemMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(QuestionItem questionItem) {
        questionItemMapper.updateById(questionItem);
    }

    /**
     * 根据ID查询
     */
    public QuestionItem selectById(Integer id) {
        return questionItemMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<QuestionItem> selectAll(QuestionItem questionItem) {
        return questionItemMapper.selectAll(questionItem);
    }

    /**
     * 分页查询
     */
    public PageInfo<QuestionItem> selectPage(QuestionItem questionItem, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionItem> list = questionItemMapper.selectAll(questionItem);
        return PageInfo.of(list);
    }

    public List<QuestionItem> selectByQuestionId(Integer questionId) {
        return questionItemMapper.selectByQuestionId(questionId);
    }
}