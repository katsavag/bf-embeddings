package com.katsadourose.knowledge_discovery_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KnowledgeSearchItemDto {

    private final String knowledgeArtifact;
    private final double score;
}
