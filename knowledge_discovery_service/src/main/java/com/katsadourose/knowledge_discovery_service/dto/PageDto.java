package com.katsadourose.knowledge_discovery_service.dto;

import java.util.List;

public record PageDto <T>(
    List<T> items,
    int pageSize,
    int totalPages,
    int page
){}
