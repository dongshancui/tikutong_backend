package com.dongshan.tikutong.model.vo;

import cn.hutool.json.JSONUtil;
import com.dongshan.tikutong.model.entity.QuestionBankQuestion;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题库题目关联表视图
 *
 * @author <a href="https://github.com/lidongshan">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Data
public class QuestionBankQuestionVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题库Id
     */
    private Long questionBankId;

    /**
     * 题目Id
     */
    private Long questionId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param questionBankQuestionVO
     * @return
     */
    public static QuestionBankQuestion voToObj(QuestionBankQuestionVO questionBankQuestionVO) {
        if (questionBankQuestionVO == null) {
            return null;
        }
        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
        BeanUtils.copyProperties(questionBankQuestionVO, questionBankQuestion);
        return questionBankQuestion;
    }

    /**
     * 对象转封装类
     *
     * @param questionBankQuestion
     * @return
     */
    public static QuestionBankQuestionVO objToVo(QuestionBankQuestion questionBankQuestion) {
        if (questionBankQuestion == null) {
            return null;
        }
        QuestionBankQuestionVO questionBankQuestionVO = new QuestionBankQuestionVO();
        BeanUtils.copyProperties(questionBankQuestion, questionBankQuestionVO);
        return questionBankQuestionVO;
    }
}