package com.dongshan.tikutong.model.dto.questionBankQuestion;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 移除题库题目关联表请求
 */
@Data
public class QuestionBankQuestionRemoveRequest implements Serializable {

    /**
     * 题库Id
     */
    private Long questionBankId;

    /**
     * 题目Id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}