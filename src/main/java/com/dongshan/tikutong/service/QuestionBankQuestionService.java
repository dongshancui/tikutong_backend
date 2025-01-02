package com.dongshan.tikutong.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dongshan.tikutong.model.dto.questionBankQuestion.QuestionBankQuestionQueryRequest;
import com.dongshan.tikutong.model.entity.QuestionBankQuestion;
import com.dongshan.tikutong.model.entity.User;
import com.dongshan.tikutong.model.vo.QuestionBankQuestionVO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 题库题目关联表服务
 *
 * @author <a href="https://github.com/lidongshan">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
public interface QuestionBankQuestionService extends IService<QuestionBankQuestion> {

    /**
     * 校验数据
     *
     * @param questionBankQuestion
     * @param add 对创建的数据进行校验
     */
    void validQuestionBankQuestion(QuestionBankQuestion questionBankQuestion, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionBankQuestionQueryRequest
     * @return
     */
    QueryWrapper<QuestionBankQuestion> getQueryWrapper(QuestionBankQuestionQueryRequest questionBankQuestionQueryRequest);
    
    /**
     * 获取题库题目关联表封装
     *
     * @param questionBankQuestion
     * @param request
     * @return
     */
    QuestionBankQuestionVO getQuestionBankQuestionVO(QuestionBankQuestion questionBankQuestion, HttpServletRequest request);

    /**
     * 分页获取题库题目关联表封装
     *
     * @param questionBankQuestionPage
     * @param request
     * @return
     */
    Page<QuestionBankQuestionVO> getQuestionBankQuestionVOPage(Page<QuestionBankQuestion> questionBankQuestionPage, HttpServletRequest request);

    /**
     * 批量添加题目到题库
     * @param questionIdList 题目id列表
     * @param questionBankId 题库id
     * @param loginUser 登录用户
     */
    void batchAddQuestionToBank(List<Long> questionIdList, Long questionBankId, User loginUser);

    /**
     * 批量从题库删除题目
     * @param questionIdList 题目id列表
     * @param questionBankId 题库id
     * @param loginUser 登录用户
     */
    void batchRemoveQuestionFromBank(List<Long> questionIdList,Long questionBankId,User loginUser);

    /**
     * 批量添加题目到题库(事务，仅供内部调用)
     * @param questionBankQuestionList
     */
    @Transactional(rollbackFor = Exception.class)
    void batchAddQuestionToBankInner(List<QuestionBankQuestion> questionBankQuestionList);

}
