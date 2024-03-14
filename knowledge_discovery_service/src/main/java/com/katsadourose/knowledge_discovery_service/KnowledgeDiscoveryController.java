package com.katsadourose.knowledge_discovery_service;

import com.katsadourose.knowledge_discovery_service.dto.*;
import com.katsadourose.knowledge_discovery_service.service.KnowledgeDiscoveryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("knowledge-artifact")
@AllArgsConstructor
public class KnowledgeDiscoveryController {

    private final KnowledgeDiscoveryService knowledgeDiscoveryService;

    @PostMapping(value = "", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void createKnowledgeArtifact(@RequestBody KnowledgeDto request) {
        knowledgeDiscoveryService.addKnowledgeToCollection(request.collection(), request.text());
    }

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<PageDto<KnowledgeDto>> getAllKnowledgeItems(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){

    }

    @PostMapping(value = "/compute-embeddings")
    public ResponseEntity<EmbeddingsDto> computeEmbeddings(@RequestBody EmbeddingsRequestDto requestDto){
        EmbeddingsDto responseDto = knowledgeDiscoveryService.computeEmbeddings(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping(value = "/similarity-search")
    public ResponseEntity<KnowledgeSearchResponseDto> computeSimilarity(@RequestBody KnowledgeRequestDto request) {
        KnowledgeSearchResponseDto response = knowledgeDiscoveryService.knowledgeSearch(request.searchText());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
