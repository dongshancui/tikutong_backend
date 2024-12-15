package com.dongshan.tikutong.model.dto.questionBankQuestion;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新题库题目关联表请求
 *
 * @author <a href="https://github.com/lidongshan">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Data
public class QuestionBankQuestionUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * notid
     */
    private Long notId;

    /**
     * 题库Id
     */
    private Long questionBankId;

    private static final long serialVersionUID = 1L;
}