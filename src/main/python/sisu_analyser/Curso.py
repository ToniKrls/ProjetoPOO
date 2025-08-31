from typing import List
from .Nota import Nota

class Curso:
    def __init__(self, nome: str, campus: str):
        self.__nome = nome
        self.__campus = campus
        self.__notas: List[Nota] = []
        
    def get_nome(self):
        return self.__nome
    
    def get_campus(self):
        return self.__campus
    
    def get_notas(self):
        return self.__notas
        
    def adicionar_nota(self, nota: Nota):
        self.__notas.append(nota)