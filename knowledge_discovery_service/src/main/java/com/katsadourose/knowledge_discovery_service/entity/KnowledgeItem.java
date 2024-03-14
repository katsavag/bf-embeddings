package com.katsadourose.knowledge_discovery_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "KnowledgeArtifacts")
public class KnowledgeItem {

    @Id
    @GeneratedValue
    @Column(name = "knowledge_artifact_id")
    private long id;

    @Column(name = "collection")
    private String collection;

    @Column(name = "knowledge_artifact")
    private String knowledgeArtifact;
}
