package com.dongshan.tikutong.constant;

/**
 * Redis 常量
 */
public interface HotkeyConstant {
    /**
     * 用户签到记录的 Redis key 前缀
     */
    public static final String QUESTION_BANK_PAGE_KEY_PREFIX = "bank_detail_";

    /**
     * 获取题库详情缓存的 key
     * @param questionBankid 题库id
     * @return
     */
    static String getQuestionBankPageKey(long questionBankid){
        return String.format("%s:%d",QUESTION_BANK_PAGE_KEY_PREFIX,questionBankid);
    }
}
