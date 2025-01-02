package com.dongshan.tikutong.job.cycle;

import cn.hutool.core.collection.CollUtil;
import com.com.dongshan.tikutong.mapper.PostMapper;
import com.dongshan.tikutong.esdao.PostEsDao;
import com.dongshan.tikutong.esdao.QuestionEsDao;
import com.dongshan.tikutong.mapper.QuestionMapper;
import com.dongshan.tikutong.model.dto.post.PostEsDTO;
import com.dongshan.tikutong.model.dto.question.QuestionEsDTO;
import com.dongshan.tikutong.model.entity.Post;
import com.dongshan.tikutong.model.entity.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 增量同步面试题目到 es
 *
 * @author <a href="https://github.com/lidongshan">程序员鱼皮</a>
 * @from <a href="https://dongshan.icu">编程导航知识星球</a>
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class IncSyncQuestionToEs {

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionEsDao questionEsDao;

    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        // 查询近 5 分钟内的数据
        Date fiveMinutesAgoDate = new Date(new Date().getTime() - 5 * 60 * 1000L);
        List<Question> postList = questionMapper.listQuestionWithDelete(fiveMinutesAgoDate);
        if (CollUtil.isEmpty(postList)) {
            log.info("no inc post");
            return;
        }
        List<QuestionEsDTO> questionEsDTOList = postList.stream()
                .map(QuestionEsDTO::objToDto)
                .collect(Collectors.toList());
        final int pageSize = 500;
        int total = questionEsDTOList.size();
        log.info("IncSyncPostToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            questionEsDao.saveAll(questionEsDTOList.subList(i, end));
        }
        log.info("IncSyncPostToEs end, total {}", total);
    }
}
