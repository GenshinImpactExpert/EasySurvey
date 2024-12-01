package com.example.mapper;

import com.example.entity.QuestionItem;
import java.util.List;

/**
 * 操作question_item相关数据接口
 */
public interface QuestionItemMapper {

    /**
     * 新增
     */
    int insert(QuestionItem questionItem);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(QuestionItem questionItem);

    /**
     * 根据ID查询
     */
    QuestionItem selectById(Integer id);

    /**
     * 查询所有
     */
    List<QuestionItem> selectAll(QuestionItem questionItem);

    List<QuestionItem> selectByQuestionId(Integer questionId);

    void deleteByQuestionId(Integer questionId);
}