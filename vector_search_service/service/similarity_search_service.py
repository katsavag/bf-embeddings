import uuid
import logging

from qdrant_client import QdrantClient
from sentence_transformers import SentenceTransformer

from protos.service_pb2_grpc import VectorSearchServiceServicer
import protos.service_pb2 as pb2
from model.bf_vector import BfVector
from qdrant_client.http.models import PointStruct
import json
from qdrant_client.http.models import VectorParams, Distance



class SimilaritySearchService(VectorSearchServiceServicer):
    _QDRANT_HOST = "localhost"
    _QDRANT_PORT = 6333
    _COLLECTION_NAME = "bf_knowledge"
    _MODEL = SentenceTransformer("multi-qa-mpnet-base-dot-v1")

    def CreateCollection(self, request, context):
        response = pb2.Void()
        self.get_db_client().create_collection(
            collection_name="bf_knowledge",
            vectors_config=VectorParams(size=768, distance=Distance.COSINE),
        )

        return response

    def CreateKnowledgeVector(self, request, context):
        response = pb2.Void()


    def ProcessEmbeddings(self, request, context):
        logging.debug("Embeddings service called.")
        response = pb2.EmbeddingsResponse()

        response.text = request.text
        embeddings = self.get_embeddings(request.text)
        response.embeddings.extend(embeddings)
        return response

    def SearchVector(self, request, context):
        vector_entry_list = []
        text_to_search: str = request.searchText
        embeddings_to_search: list = self.get_embeddings(text_to_search)

        qdrant_client = self.get_db_client()
        results = qdrant_client.search(collection_name="bf_knowledge",
                                       query_vector=embeddings_to_search,
                                       limit=3)
        for result in results:
            result_json = json.loads(result.json())
            entry = pb2.VectorEntry(
                paperTitle=result_json["payload"]["paper_title"],
                text=result_json["payload"]["text"],
                score=result_json["score"]
            )
            vector_entry_list.append(entry)

        return pb2.SimilaritySearchResponse(
            entries=vector_entry_list
        )

    def get_embeddings(self, text: str) -> list:
        return self._MODEL.encode(text).tolist()

    def get_db_client(self) -> QdrantClient:
        return QdrantClient(self._QDRANT_HOST, port=self._QDRANT_PORT)

    def create_entry(self, bf_vector: BfVector) -> None:
        id = uuid.uuid4()

        self.get_db_client().upsert(
            collection_name=self._COLLECTION_NAME,
            wait=True,
            points=[
                PointStruct(id=str(id), vector=bf_vector.get_vector(), payload=bf_vector.get_payload())
            ]
        )

        return id
