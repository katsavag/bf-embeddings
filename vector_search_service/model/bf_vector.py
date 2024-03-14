class BfVector:

    def __init__(self, vector: list, payload: dict):
        self._vector: list = vector
        self._payload: dict = payload

    def get_vector(self):
        return self._vector

    def get_payload(self):
        return self._payload
