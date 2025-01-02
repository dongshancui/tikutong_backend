package com.dongshan.tikutong.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 删除题目请求
 */
@Data
public class QuestionBatchDeleteRequest implements Serializable {

    private List<Long> questionIdList;

    private static final long serialVersionUID = 1L;
}