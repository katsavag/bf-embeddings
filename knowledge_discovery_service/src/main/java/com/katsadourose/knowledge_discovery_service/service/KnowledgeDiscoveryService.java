package com.katsadourose.knowledge_discovery_service.service;

import com.katsadourose.knowledge_discovery_service.dto.*;
import com.katsadourose.knowledge_discovery_service.entity.KnowledgeItem;
import org.springframework.data.domain.Pageable;

public interface KnowledgeDiscoveryService {

    void createNewCollection();
    void addKnowledgeToCollection(String collection, String knowledge);
    PageDto<KnowledgeItem> getKnowledgeByPage(Pageable page, String collection);
    PageDto<KnowledgeItem> getAllKnowledge(Pageable pageable);
    KnowledgeSearchResponseDto knowledgeSearch(String searchText);
    EmbeddingsDto computeEmbeddings(EmbeddingsRequestDto embeddingsRequestDto);
}
