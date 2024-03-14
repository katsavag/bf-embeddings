import sys
from concurrent import futures

import grpc
import logging

from qdrant_client import QdrantClient
from qdrant_client.http import models
from qdrant_client.http.exceptions import UnexpectedResponse
from qdrant_client.http.models import VectorParams, Distance

from protos.service_pb2_grpc import add_VectorSearchServiceServicer_to_server
import pandas as pd
from service.similarity_search_service import SimilaritySearchService
from model.bf_vector import BfVector

_QDRANT_HOST = "localhost"
_QDRANT_PORT = 6333
_COLLECTION_NAME = "bf_knowledge"

initialization_retries = 0


def get_db_client() -> QdrantClient:
    return QdrantClient(_QDRANT_HOST, port=_QDRANT_PORT)


def initialization(vector_search_service: SimilaritySearchService) -> None:
    client = get_db_client()
    global initialization_retries

    try:
        print("Create collection")
        bf_knowledge_df = pd.read_csv("./data/bf_knowledge.csv")
        list_of_text = list(bf_knowledge_df["text"])
        client.create_collection(
            collection_name="bf_knowledge",
            vectors_config=VectorParams(size=768, distance=Distance.COSINE),
        )

        for item in list_of_text:
            embeddings = vector_search_service.get_embeddings(item)
            bf_vector = BfVector(vector=embeddings,
                                 payload={"paper_title": "test", "text": item})
            print("Create Entry")
            vector_search_service.create_entry(bf_vector)
    except UnexpectedResponse as ex:
        print(ex)
        logging.info("Collection doesnt exist")
        logging.info("Create collection.")
        client.create_collection(
            collection_name=_COLLECTION_NAME,
            vectors_config=models.VectorParams(size=768, distance=models.Distance.COSINE)
        )
        initialization_retries = initialization_retries + 1
        if initialization_retries < 2:
            initialization(vector_search_service=vector_search_service)


def serve():
    logging.basicConfig(filename='app.log',
                        filemode='w',
                        format='%(name)s - %(levelname)s - %(message)s',
                        level=logging.DEBUG,
                        )
    root = logging.getLogger()
    handler = logging.StreamHandler(sys.stdout)
    root.addHandler(handler)

    # initialization(embeddings_service=embeddings_service, vector_search_service=vector_search_service)
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    add_VectorSearchServiceServicer_to_server(SimilaritySearchService(), server)

    server.add_insecure_port('[::]:9010')
    server.start()
    logging.info("gRPC Server started...")
    server.wait_for_termination()


if __name__ == '__main__':
    print("Server started...")
    serve()

