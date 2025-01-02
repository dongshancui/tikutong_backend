package com.dongshan.tikutong.model.dto.questionBankQuestion;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建题库题目关联表请求
 *
 * @author <a href="https://github.com/lidongshan">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Data
public class QuestionBankQuestionBatchAddRequest implements Serializable {

    /**
     * 题库Id
     */
    private Long questionBankId;

    /**
     * 题目Id
     */
    private List<Long> questionIdList;


    private static final long serialVersionUID = 1L;
}