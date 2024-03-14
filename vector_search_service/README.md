mkdir proto_generated
python -m grpc_tools.protoc -I . --proto_path=./protos --python_out=. --grpc_python_out=. protos/service.proto
