import re
import os
from typing import List
from .Curso import Curso
from .Nota import Nota

class SisuService:
    def carregarCursos(self, caminho_arquivo: str):
        cursos = []
        
        try:
            if not os.path.isabs(caminho_arquivo):
                # Calcula o caminho para a pasta resources
                current_dir = os.path.dirname(os.path.dirname(os.path.dirname(__file__)))
                resources_path = os.path.join(current_dir, '..', '..', 'resources', caminho_arquivo)
                resources_path = os.path.abspath(resources_path)
                
                if os.path.exists(resources_path):
                    caminho_arquivo = resources_path
                else:
                    # Tenta encontrar na pasta atual como fallback
                    current_path = os.path.join(os.path.dirname(__file__), caminho_arquivo)
                    if os.path.exists(current_path):
                        caminho_arquivo = current_path
            
            print(f"Tentando abrir: {caminho_arquivo}")
            
            with open(caminho_arquivo, "r", encoding="utf-8") as f:
                dados_iniciaram = False
                
                for linha in f:
                    linha = linha.strip()
                    if not linha:
                        continue
                    
                    if "N. ENEM" in linha and "Curso" in linha:
                        dados_iniciaram = True
                        continue
                    
                    if not dados_iniciaram:
                        continue
                    
                    try:
                        # Processar linha usando regex para dividir por múltiplos espaços
                        colunas = re.split(r'\s{2,}', linha)
                        
                        if len(colunas) < 7:
                            continue
                        
                        # Extrair informações relevantes
                        curso_nome = colunas[2].strip()
                        campus = colunas[3].strip()
                        demanda = colunas[4].strip()
                        # Tratar tanto "A0" quanto "AC" como Ampla Concorrência
                        if demanda == "A0":
                            demanda = "AC"
                        media_str = colunas[5].replace(".", "").replace(",", ".")
                        media = float(media_str)
                        colocacao_str = colunas[6].replace("º", "").replace("°", "").strip()
                        colocacao = int(colocacao_str) if colocacao_str.isdigit() else 0

                        curso = self._encontrar_ou_criar(cursos, curso_nome, campus)
                        nota = Nota(2024, demanda, media, colocacao)
                        curso.adicionar_nota(nota)
                    
                    except Exception as e:
                        print(f"⚠️ Erro ao processar linha: {linha} ({e})")

        except Exception as e:
            print(f"❌ Erro ao ler arquivo: {e}")

        return cursos
        
    def _encontrar_ou_criar(self, cursos, nome, campus):
        for c in cursos:
            if c.get_nome().lower() == nome.lower() and c.get_campus().lower() == campus.lower():
                return c 
        novo = Curso(nome, campus)
        cursos.append(novo)
        return novo