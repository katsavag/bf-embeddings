package com.katsadourose.knowledge_discovery_service.repository;

import com.katsadourose.knowledge_discovery_service.entity.KnowledgeItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public interface KnowledgeItemRepository extends JpaRepository<KnowledgeItem, Long> {

    @Query(
            value = "select ki from KnowledgeItem ki where ki.collection = ?1",
            countQuery = "select count (ki) from KnowledgeItem ki where ki.collection = ?1"
    )
    Page<KnowledgeItem> findAllByCollectionWithPagination(String collection, Pageable page);
}
