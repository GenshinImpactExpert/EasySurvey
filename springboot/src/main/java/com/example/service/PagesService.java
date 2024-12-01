package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.LogsTypeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Pages;
import com.example.entity.Question;
import com.example.entity.QuestionItem;
import com.example.mapper.PagesMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 问卷表业务处理
 **/
@Service
public class PagesService {

    @Resource
    private PagesMapper pagesMapper;

    @Resource
    QuestionService questionService;

    @Resource
    QuestionItemService questionItemService;
    /**
     * 新增
     */
    /**
     * 新增
     */
    public void add(Pages pages) {

        Account currentUser = TokenUtils.getCurrentUser();
        System.out.println(11);
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            pages.setUserId(currentUser.getId());

        }
        pages.setCreateTime(DateUtil.now());

        pagesMapper.insert(pages);
        LogsService.recordLogs("创建问卷【" + pages.getName() + "】", LogsTypeEnum.ADD.getValue(), currentUser.getUsername());

    }

    /**
     * 删除
     */

    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();

        Pages pages = this.selectById(id);
        pagesMapper.deleteById(id);

        questionService.deleteByPageId(id);
        //添加日志
        LogsService.recordLogs("删除问卷【" + pages.getName() + "】", LogsTypeEnum.DELETE.getValue(), currentUser.getUsername());

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
    public void updateById(Pages pages) {
        Account currentUser = TokenUtils.getCurrentUser();

        pagesMapper.updateById(pages);
        LogsService.recordLogs("修改问卷【" + pages.getName() + "】", LogsTypeEnum.UPDATE.getValue(), currentUser.getUsername());

    }

    /**
     * 根据ID查询
     */
    public Pages selectById(Integer id) {
        return pagesMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Pages> selectAll(Pages pages) {
        return pagesMapper.selectAll(pages);
    }

    /**
     * 分页查询
     */
    public PageInfo<Pages> selectPage(Pages pages, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){//如果是用户筛选出当前用户问卷信息
            pages.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);

        List<Pages> list = pagesMapper.selectAll(pages);
        System.out.println("list is: " + list);
        return PageInfo.of(list);
    }

    @Transactional
    public void copy(Integer pageId) {
        Pages pages = this.selectById(pageId);//拿到当前被拷贝的问卷的内容
        pages.setCount(pages.getCount() + 1);
        this.updateById(pages);//更新使用次数

        Pages newPage = new Pages();
        newPage.setName(pages.getName() + "拷贝");
//        Account currentUser=TokenUtils.getCurrentUser();
//        newPage.setUserId(currentUser.getId());//注意复制之后的问卷应该有新的用户id
        newPage.setDescr(pages.getDescr());
        newPage.setImg(pages.getImg());
        this.add(newPage);
        List<Question> questionList = questionService.selectByPageId(pageId);//拿到当前问卷关联的所有题目信息
        for (Question question : questionList) {
            question.setId(null);//清除院校的主键，因为要插入新的数据
            question.setPageId(newPage.getId());//设置题目的问卷id
            questionService.add(question);//插入数据到数据库
            List<QuestionItem> questionItemList = question.getQuestionItemList();
            for (QuestionItem questionItem : questionItemList) {
                questionItem.setId(null);
                questionItem.setQuestionId(question.getId());
                questionItemService.add(questionItem);
            }
        }
        LogsService.recordLogs("复制新的问卷【" + newPage.getName() + "】", LogsTypeEnum.COPY.getValue(), TokenUtils.getCurrentUser().getUsername());
    }
}
