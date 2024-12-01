package com.example.service;

import com.example.entity.Answer;
import com.example.mapper.AnswerMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 业务处理
 **/
@Service
public class AnswerService {

    @Resource
    private AnswerMapper answerMapper;

    /**
     * 新增
     */
    public void add(Answer answer) {
        answerMapper.insert(answer);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        answerMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            answerMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Answer answer) {
        answerMapper.updateById(answer);
    }

    /**
     * 根据ID查询
     */
    public Answer selectById(Integer id) {
        return answerMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Answer> selectAll(Answer answer) {
        return answerMapper.selectAll(answer);
    }

    /**
     * 分页查询
     */
    public PageInfo<Answer> selectPage(Answer answer, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Answer> list = answerMapper.selectAll(answer);
        return PageInfo.of(list);
    }

}
