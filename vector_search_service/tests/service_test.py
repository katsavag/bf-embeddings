import unittest

import grpc
from sentence_transformers import SentenceTransformer

from protos.service_pb2_grpc import VectorSearchServiceStub
from service.similarity_search_service import SimilaritySearchService
import protos.service_pb2 as pb2


# class ServiceTests(unittest.TestCase):
#     SERVICE_URL = "localhost:9010"
#     _MODEL = SentenceTransformer("multi-qa-mpnet-base-dot-v1")
#
#     def test_embeddings(self):
#         channel = grpc.insecure_channel(self.SERVICE_URL)
#         stub = EmbeddingsStub(channel)
#         test_text = "Memory class information"
#         request = pb2.EmbeddingsRequest(text=test_text)
#         result = stub.ProcessEmbeddings(request)
#         print(result.embeddings)
#
#
#     def test_search(self):
#         channel = grpc.insecure_channel(self.SERVICE_URL)
#         stub = VectorSearchStub(channel)
#         embeddings_service = EmbeddingsService()
#         test_text = "Memory class information"
#         embeddings = embeddings_service.get_embeddings(test_text)
#
#         request = pb2.SimilaritySearchRequest(embeddings=embeddings)
#         response = stub.SearchVector(request)
#
#         for entry in response.entries.entries:
#             print("entry")
#             print("Paper Title:", entry.paperTitle)
#             print("Text:", entry.text)
#             print("Embeddings:", entry.embeddings)
#
#         assert len(response.entries.entries) > 0
#         assert embeddings is not None


SERVICE_URL = "localhost:9010"
_MODEL = SentenceTransformer("multi-qa-mpnet-base-dot-v1")
channel = grpc.insecure_channel(SERVICE_URL)
stub = VectorSearchServiceStub(channel)
embeddings_service = SimilaritySearchService()
test_text = "Memory class information"
# embeddings = embeddings_service.get_embeddings(test_text)

request = pb2.SimilaritySearchRequest(searchText=test_text)
response = stub.SearchVector(request)

print("response")
print(response)

for entry in response.entries:
    print("entry")
    print("Paper Title:", entry.paperTitle)
    print("Text:", entry.text)

