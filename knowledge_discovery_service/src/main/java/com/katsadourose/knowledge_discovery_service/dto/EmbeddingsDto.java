package com.katsadourose.knowledge_discovery_service.dto;

import java.util.List;

public record EmbeddingsDto(
        String text,
        List<Float> embeddings
) {
}
