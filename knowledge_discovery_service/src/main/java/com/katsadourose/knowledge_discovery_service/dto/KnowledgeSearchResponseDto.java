package com.katsadourose.knowledge_discovery_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class KnowledgeSearchResponseDto {

    private final String searchText;
    private final List<KnowledgeSearchItemDto> searchResults;
}
