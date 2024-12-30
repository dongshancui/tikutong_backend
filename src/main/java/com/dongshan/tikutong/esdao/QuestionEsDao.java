package com.dongshan.tikutong.esdao;

import com.dongshan.tikutong.model.dto.post.PostEsDTO;
import com.dongshan.tikutong.model.dto.question.QuestionEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface QuestionEsDao extends ElasticsearchRepository<QuestionEsDTO, Long> {
}
