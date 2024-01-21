from concurrent import futures

from embeddings_pb2_grpc import add_EmbeddingsServicer_to_server
from embeddings_service import EmbeddingsServicer


import grpc
import logging


def serve():
    logging.basicConfig(filename='app.log',
                        filemode='w',
                        format='%(name)s - %(levelname)s - %(message)s',
                        level=logging.DEBUG)

    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    add_EmbeddingsServicer_to_server(EmbeddingsServicer(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    logging.info("gRPC Server started...")
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
