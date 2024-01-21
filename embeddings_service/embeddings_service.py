import logging

from embeddings_pb2_grpc import EmbeddingsServicer
import embeddings_pb2 as pb2


class EmbeddingsService(EmbeddingsServicer):
    def ProcessEmbeddings(self, request, context):
        logging.debug("Embeddings service called.")
        request.text
        return pb2.EmbeddingsResponse(
            text=request.text,
            embeddings=[0.0, 0.0]
        )
