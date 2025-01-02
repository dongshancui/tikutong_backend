package com.dongshan.tikutong.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dongshan.tikutong.common.ErrorCode;
import com.dongshan.tikutong.constant.CommonConstant;
import com.dongshan.tikutong.exception.BusinessException;
import com.dongshan.tikutong.exception.ThrowUtils;
import com.dongshan.tikutong.mapper.QuestionBankQuestionMapper;
import com.dongshan.tikutong.model.dto.questionBankQuestion.QuestionBankQuestionQueryRequest;
import com.dongshan.tikutong.model.entity.Question;
import com.dongshan.tikutong.model.entity.QuestionBank;
import com.dongshan.tikutong.model.entity.QuestionBankQuestion;
import com.dongshan.tikutong.model.entity.User;
import com.dongshan.tikutong.model.vo.QuestionBankQuestionVO;
import com.dongshan.tikutong.model.vo.UserVO;
import com.dongshan.tikutong.service.QuestionBankQuestionService;
import com.dongshan.tikutong.service.QuestionBankService;
import com.dongshan.tikutong.service.QuestionService;
import com.dongshan.tikutong.service.UserService;
import com.dongshan.tikutong.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 题库题目关联表服务实现
 *
 * @author <a href="https://github.com/lidongshan">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Service
@Slf4j
public class QuestionBankQuestionServiceImpl extends ServiceImpl<QuestionBankQuestionMapper, QuestionBankQuestion> implements QuestionBankQuestionService {

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private QuestionService questionService;


    @Resource
    private QuestionBankService questionBankService;

    /**
     * 校验数据
     *
     * @param questionBankQuestion
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validQuestionBankQuestion(QuestionBankQuestion questionBankQuestion, boolean add) {
        ThrowUtils.throwIf(questionBankQuestion == null, ErrorCode.PARAMS_ERROR);
        // 校验题目和题库是否存在
        Long questionBankId = questionBankQuestion.getQuestionBankId();
        Long questionId = questionBankQuestion.getQuestionId();

        if(questionId != null){
            Question question = questionService.getById(questionId);
            if(question == null){
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
            }
        }
        if(questionBankId != null){
            QuestionBank questionBank = questionBankService.getById(questionBankId);
            if(questionBank == null){
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题库不存在");
            }
        }




//        String title = questionBankQuestion.getTitle();
//        // 创建数据时，参数不能为空
//        if (add) {
//            // todo 补充校验规则
//            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
//        }
//        // 修改数据时，有参数则校验
//        // todo 补充校验规则
//        if (StringUtils.isNotBlank(title)) {
//            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
//        }
    }

    /**
     * 获取查询条件
     *
     * @param questionBankQuestionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionBankQuestion> getQueryWrapper(QuestionBankQuestionQueryRequest questionBankQuestionQueryRequest) {
        QueryWrapper<QuestionBankQuestion> queryWrapper = new QueryWrapper<>();
        if (questionBankQuestionQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = questionBankQuestionQueryRequest.getId();
        Long notId = questionBankQuestionQueryRequest.getNotId();
        String sortField = questionBankQuestionQueryRequest.getSortField();
        String sortOrder = questionBankQuestionQueryRequest.getSortOrder();
        Long userId = questionBankQuestionQueryRequest.getUserId();
        Long questionBankId = questionBankQuestionQueryRequest.getQuestionBankId();
        Long questionId = questionBankQuestionQueryRequest.getQuestionId();

        int current = questionBankQuestionQueryRequest.getCurrent();
        int pageSize = questionBankQuestionQueryRequest.getPageSize();



        // todo 补充需要的查询条件
        // 从多字段中搜索
//        if (StringUtils.isNotBlank(searchText)) {
//            // 需要拼接查询条件
//            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
//        }
        // 模糊查询
//        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
//        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
//        // JSON 数组查询
//        if (CollUtil.isNotEmpty(tagList)) {
//            for (String tag : tagList) {
//                queryWrapper.like("tags", "\"" + tag + "\"");
//            }
//        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionBankId), "questionBankId", questionBankId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取题库题目关联表封装
     *
     * @param questionBankQuestion
     * @param request
     * @return
     */
    @Override
    public QuestionBankQuestionVO getQuestionBankQuestionVO(QuestionBankQuestion questionBankQuestion, HttpServletRequest request) {
        // 对象转封装类
        QuestionBankQuestionVO questionBankQuestionVO = QuestionBankQuestionVO.objToVo(questionBankQuestion);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = questionBankQuestion.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionBankQuestionVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        // endregion

        return questionBankQuestionVO;
    }

    /**
     * 分页获取题库题目关联表封装
     *
     * @param questionBankQuestionPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionBankQuestionVO> getQuestionBankQuestionVOPage(Page<QuestionBankQuestion> questionBankQuestionPage, HttpServletRequest request) {
        List<QuestionBankQuestion> questionBankQuestionList = questionBankQuestionPage.getRecords();
        Page<QuestionBankQuestionVO> questionBankQuestionVOPage = new Page<>(questionBankQuestionPage.getCurrent(), questionBankQuestionPage.getSize(), questionBankQuestionPage.getTotal());
        if (CollUtil.isEmpty(questionBankQuestionList)) {
            return questionBankQuestionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionBankQuestionVO> questionBankQuestionVOList = questionBankQuestionList.stream().map(questionBankQuestion -> {
            return QuestionBankQuestionVO.objToVo(questionBankQuestion);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionBankQuestionList.stream().map(QuestionBankQuestion::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // endregion

        questionBankQuestionVOPage.setRecords(questionBankQuestionVOList);
        return questionBankQuestionVOPage;
    }

    /**
     * 批量添加题目到题库
     * @param questionIdList 题目id列表
     * @param questionBankId 题库id
     * @param loginUser 登录用户
     */
    @Override
    public void batchAddQuestionToBank(List<Long> questionIdList, Long questionBankId, User loginUser) {
        // 参数校验合法
        ThrowUtils.throwIf(questionIdList == null ||questionIdList.isEmpty(),ErrorCode.PARAMS_ERROR,"题目列表不能为空");
        ThrowUtils.throwIf(questionBankId == null || questionBankId <= 0,ErrorCode.PARAMS_ERROR,"题库列表不合法");
        ThrowUtils.throwIf(loginUser == null,ErrorCode.PARAMS_ERROR,"用户未登录（权限错误）");
        // 过滤无效的id列表
        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = Wrappers.lambdaQuery(Question.class)
                .select(Question::getId)
                .in(Question::getId, questionIdList);
        List<Long> validQuestionIdList = questionService.listObjs(questionLambdaQueryWrapper,obj -> (Long)obj);
        ThrowUtils.throwIf(validQuestionIdList.isEmpty(),ErrorCode.PARAMS_ERROR,"合法的题目id列表为空！");
        // 检查题库id是否合法
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        ThrowUtils.throwIf(questionBank == null,ErrorCode.PARAMS_ERROR,"题库不存在");
        // 对于已存在的题库题库关联，避免重复插入，进行去重操作
        LambdaQueryWrapper<QuestionBankQuestion> questionBankQuestionLambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                .eq(QuestionBankQuestion::getQuestionBankId, questionBankId);
        List<Long> existQuestionIdInBank = this.listObjs(questionBankQuestionLambdaQueryWrapper,obj -> (Long)obj);
        Set<Long> existQuestionIdSet = existQuestionIdInBank.stream().collect(Collectors.toSet());
        List<Long> uniqueQuestionIdList = validQuestionIdList.stream()
                .filter(id -> !existQuestionIdSet.contains(id))
                .collect(Collectors.toList());
        ThrowUtils.throwIf(uniqueQuestionIdList.isEmpty(),ErrorCode.PARAMS_ERROR,"题目已存在与题库中，无需重复添加");

        // 自定义线程池(IO密集型线程池)
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                20,                    // 核心线程数
                50,                    // 最大线程数
                60L,                    // 存活时间
                TimeUnit.SECONDS,      // 存活时间单位
                new LinkedBlockingDeque<>(10000),    // 阻塞队列
                new ThreadPoolExecutor.CallerRunsPolicy()   // 拒绝策略，由调用线程处理任务
        );

        // 保存所有批次任务
        List<CompletableFuture<Void>> futures = new ArrayList<>();


        // 执行添加操作
        int batchSize = 1000;
//        int batchSize = 1;
        int totalSize = questionIdList.size();
        for(int i  = 0;i < totalSize;i += batchSize){
            List<Long> subList = uniqueQuestionIdList.subList(i, Math.min(i + batchSize, totalSize));
            List<QuestionBankQuestion> questionBankQuestions = subList.stream()
                    .map(questionId -> {
                        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
                        questionBankQuestion.setQuestionBankId(questionBankId);
                        questionBankQuestion.setQuestionId(questionId);
                        questionBankQuestion.setUserId(loginUser.getId());
                        return questionBankQuestion;
                    }).collect(Collectors.toList());
            // 使用事务处理每批数据
            // 获取代理：由于spring的特性，是通过代理类实现的，因此不能直接调用，而是要从bean容器中找到对应的代理类调用方法。
            QuestionBankQuestionService proxy = (QuestionBankQuestionService) AopContext.currentProxy();
            // 添加任务到线程池中
            CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
                proxy.batchAddQuestionToBankInner(questionBankQuestions);
            }, poolExecutor);
            futures.add(task);
        }
        // 等待所有批次都完成操作
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        // 关闭线程池
        poolExecutor.shutdown();
    }

    /**
     * 批量添加题目到题库(事务，仅供内部调用)
     * @param questionBankQuestionList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAddQuestionToBankInner(List<QuestionBankQuestion> questionBankQuestionList){
        try {
            boolean result = this.saveBatch(questionBankQuestionList);
            if (!result) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "向题库添加题目失败");
            }
        } catch (DataIntegrityViolationException e) {
            log.error("数据库唯一键冲突或违反其他完整性约束,错误信息: {}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目已存在于该题库，无法重复添加");
        } catch (DataAccessException e) {
            log.error("数据库连接问题、事务问题等导致操作失败,错误信息: {}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "数据库操作失败");
        } catch (Exception e) {
            // 捕获其他异常，做通用处理
            log.error("添加题目到题库时发生未知错误,错误信息: {}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "向题库添加题目失败");
        }

    }

    /**
     * 批量从题库删除题目
     * @param questionIdList 题目id列表
     * @param questionBankId 题库id
     * @param loginUser 登录用户
     */
    @Override
    @Transactional
    public void batchRemoveQuestionFromBank(List<Long> questionIdList, Long questionBankId, User loginUser) {
        // 参数校验合法
        ThrowUtils.throwIf(questionIdList == null ||questionIdList.isEmpty(),ErrorCode.PARAMS_ERROR,"题目列表不能为空");
        ThrowUtils.throwIf(questionBankId == null || questionBankId <= 0,ErrorCode.PARAMS_ERROR,"题库列表不合法");
        ThrowUtils.throwIf(loginUser == null,ErrorCode.PARAMS_ERROR,"用户未登录（权限错误）");
        //
        for (Long questionId : questionIdList) {
            LambdaQueryWrapper<QuestionBankQuestion> wrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                    .eq(QuestionBankQuestion::getQuestionId, questionId)
                    .eq(QuestionBankQuestion::getQuestionBankId, questionBankId);
            boolean result = this.remove(wrapper);
            if(!result){
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"从题库删除题目失败");
            }
        }
    }
}
