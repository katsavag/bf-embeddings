package com.katsadourose.knowledge_discovery_service.service;

import com.katsadourose.knowledge_discovery_service.dto.*;
import com.katsadourose.knowledge_discovery_service.entity.KnowledgeItem;
import com.katsadourose.knowledge_discovery_service.repository.KnowledgeItemRepository;
import com.katsadourose.remote_services.SimilaritySearchProto;
import com.katsadourose.remote_services.VectorSearchServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class KnowledgeDiscoveryServiceImpl implements KnowledgeDiscoveryService {

    private final KnowledgeItemRepository knowledgeItemRepository;

    @Override
    public void createNewCollection() {

    }

    @Override
    public void addKnowledgeToCollection(String collection, String knowledge) {

    }

    @Override
    public PageDto<KnowledgeItem> getKnowledgeByPage(Pageable page, String collection) {

        Page<KnowledgeItem> knowledgeItemPage = knowledgeItemRepository.findAllByCollectionWithPagination(collection, page);

        return new PageDto<>(
                knowledgeItemPage.getContent(),
                knowledgeItemPage.getSize(),
                knowledgeItemPage.getTotalPages(),
                knowledgeItemPage.getNumber()
        );
    }

    @Override
    public PageDto<KnowledgeItem> getAllKnowledge(Pageable pageable) {
        Page<KnowledgeItem> knowledgeItemPage = knowledgeItemRepository.findAll(pageable);
        return new PageDto<>(
                knowledgeItemPage.getContent(),
                knowledgeItemPage.getSize(),
                knowledgeItemPage.getTotalPages(),
                knowledgeItemPage.getNumber()
        );
    }

    @Override
    public KnowledgeSearchResponseDto knowledgeSearch(String searchText) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9010)
                .usePlaintext()
                .build();

        VectorSearchServiceGrpc.VectorSearchServiceBlockingStub stub = VectorSearchServiceGrpc.newBlockingStub(channel);

        SimilaritySearchProto.SimilaritySearchRequest searchRequest = SimilaritySearchProto.SimilaritySearchRequest.newBuilder()
                .setSearchText(searchText)
                .build();

        SimilaritySearchProto.SimilaritySearchResponse response = stub.searchVector(searchRequest);

        List<KnowledgeSearchItemDto> items = response.getEntriesList()
                .stream()
                .map(vectorEntry -> new KnowledgeSearchItemDto(vectorEntry.getText(), vectorEntry.getScore()))
                .toList();

        channel.shutdown();
        return new KnowledgeSearchResponseDto(searchText, items);
    }

    @Override
    public EmbeddingsDto computeEmbeddings(EmbeddingsRequestDto embeddingsRequestDto) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9010)
                .usePlaintext()
                .build();
        VectorSearchServiceGrpc.VectorSearchServiceBlockingStub stub = VectorSearchServiceGrpc.newBlockingStub(channel);
        SimilaritySearchProto.EmbeddingsRequest request = SimilaritySearchProto.EmbeddingsRequest.newBuilder()
                .setText(embeddingsRequestDto.text())
                .build();
        SimilaritySearchProto.EmbeddingsResponse response = stub.processEmbeddings(request);

        return new EmbeddingsDto(
                embeddingsRequestDto.text(),
                response.getEmbeddingsList()
        );
    }
}
