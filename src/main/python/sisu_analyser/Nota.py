class Nota:
    def __init__(self, ano: int, demanda: str, media: float, colocacao: int):
        self.__ano = ano
        self.__demanda = demanda
        self.__media = media
        self.__colocacao = colocacao
        
    def get_ano(self):
        return self.__ano
        
    def get_demanda(self):
        return self.__demanda
        
    def get_media(self):
        return self.__media
        
    def get_colocacao(self):
        return self.__colocacao