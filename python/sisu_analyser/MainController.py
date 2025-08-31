import tkinter as tk
from tkinter import ttk, filedialog
from .SisuService import SisuService

class Main(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("SISU - Análise Exploratória")
        self.geometry("1000x700")
        self.cursos = []
        self._setup_ui()

    def _setup_ui(self):
        # Frame para botão de carregamento
        self.frame_top = tk.Frame(self)
        self.frame_top.pack(fill="x", padx=10, pady=10)
        
        self.btn_carregar = tk.Button(self.frame_top, text="Carregar Arquivo CSV", command=self._carregar_arquivo)
        self.btn_carregar.pack()
        
        # Notebook para os gráficos
        self.notebook = ttk.Notebook(self)
        self.notebook.pack(expand=True, fill="both")

        # Criação de abas e canvas
        self.canvas_top10_ampla = tk.Canvas(self.notebook, width=900, height=520, bg="white")
        self.canvas_top10_cotas = tk.Canvas(self.notebook, width=900, height=520, bg="white")
        self.canvas_campus = tk.Canvas(self.notebook, width=900, height=520, bg="white")
        self.canvas_ampla_vs_cotas = tk.Canvas(self.notebook, width=900, height=520, bg="white")

        self.notebook.add(self.canvas_top10_ampla, text="Top 10 Ampla")
        self.notebook.add(self.canvas_top10_cotas, text="Top 10 Cotas")
        self.notebook.add(self.canvas_campus, text="Campus")
        self.notebook.add(self.canvas_ampla_vs_cotas, text="Ampla x Cotas")

    def _carregar_arquivo(self):
        caminho_arquivo = filedialog.askopenfilename(
            title="Selecione o arquivo CSV",
            filetypes=[("CSV files", "*.csv"), ("All files", "*.*")]
        )
        
        if caminho_arquivo:
            service = SisuService()
            self.cursos = service.carregarCursos(caminho_arquivo)
            self._desenhar_graficos()
            self.title(f"SISU - Análise Exploratória - {caminho_arquivo}")

    def _desenhar_graficos(self):
        self.gerar_grafico_top10_ampla(self.canvas_top10_ampla)
        self.gerar_grafico_top10_cotas(self.canvas_top10_cotas)
        self.gerar_grafico_campus(self.canvas_campus)
        self.gerar_grafico_ampla_vs_cotas(self.canvas_ampla_vs_cotas)

    def gerar_grafico_top10_ampla(self, canvas):
        notas_corte = []
        
        for curso in self.cursos:
            notas = [n.get_media() for n in curso.get_notas() if n.get_demanda() == "AC"]
            
            if notas:
                nota_corte = min(notas)
                notas_corte.append((curso.get_nome(), nota_corte))
        
        notas_corte = sorted(notas_corte, key=lambda x: -x[1])[:10]
        
        if not notas_corte:
            canvas.create_text(200, 100, text="Sem dados suficientes")
            return
        
        self._desenhar_grafico_linha(canvas, notas_corte, "Top 10 Notas de Corte - Ampla Concorrência", "blue")

    def gerar_grafico_top10_cotas(self, canvas):
        notas_corte = []
        
        for curso in self.cursos:
            notas = [n.get_media() for n in curso.get_notas() if n.get_demanda() != "AC"]
            
            if notas:
                nota_corte = min(notas)
                notas_corte.append((curso.get_nome(), nota_corte))
        
        notas_corte = sorted(notas_corte, key=lambda x: -x[1])[:10]
        
        if not notas_corte:
            canvas.create_text(200, 100, text="Sem dados suficientes")
            return
        
        self._desenhar_grafico_linha(canvas, notas_corte, "Top 10 Notas de Corte - Cotas", "red")

    def _desenhar_grafico_linha(self, canvas, dados, titulo, cor):
        canvas.delete("all")
        
        largura = 800
        altura = 450
        margem = 80

        max_y = max([d[1] for d in dados]) * 1.1  # 10% de margem no topo
        min_y = min([d[1] for d in dados]) * 0.9  # 10% de margem na base
        
        escala_y = (altura - margem*2) / (max_y - min_y)
        passo_x = (largura - margem*2) / (len(dados)-1)

        # Eixo Y
        canvas.create_line(margem, margem, margem, altura-margem, width=2)
        # Eixo X
        canvas.create_line(margem, altura-margem, largura-margem, altura-margem, width=2)

        # Pontos e linhas
        pontos = []
        for i, (nome, nota) in enumerate(dados):
            x = margem + i * passo_x
            y = altura - margem - (nota - min_y) * escala_y
            pontos.append((x, y))
            
            canvas.create_oval(x-3, y-3, x+3, y+3, fill=cor, outline=cor)
            
            # Nome do curso (abreviado se necessário)
            nome_abreviado = nome[:15] + "..." if len(nome) > 15 else nome
            canvas.create_text(x, altura-margem+15, text=nome_abreviado, font=("Arial", 8), angle=45, anchor="ne")
            
            # Valor da nota
            canvas.create_text(x, y-15, text=f"{nota:.1f}", font=("Arial", 8))

        # Linha conectando os pontos
        for i in range(1, len(pontos)):
            canvas.create_line(pontos[i-1][0], pontos[i-1][1], pontos[i][0], pontos[i][1], fill=cor, width=2)

        canvas.create_text(largura//2, 20, text=titulo, font=("Arial", 12, "bold"))

    def gerar_grafico_campus(self, canvas):
        canvas.delete("all")
        
        contagem = {}
        
        for curso in self.cursos:
            campus = curso.get_campus()
            contagem[campus] = contagem.get(campus, 0) + 1 
                
        total = sum(contagem.values())
        if total == 0:
            canvas.create_text(200, 100, text="Sem dados")
            return
        
        # Cores para o gráfico de pizza
        cores = ["red", "blue", "green", "yellow", "purple", "orange", "pink", "brown", "gray", "cyan"]
        
        x = 400
        y = 250
        r = 150
        ang_inicial = 0
        
        for i, (campus, qtd) in enumerate(contagem.items()):
            fracao = qtd/total
            ang_final = ang_inicial + fracao*360

            canvas.create_arc(x-r, y-r, x+r, y+r, start=ang_inicial, extent=fracao*360,
                          fill=cores[i % len(cores)], outline="black")
            
            # Legenda
            canvas.create_rectangle(600, 100 + i*30, 620, 120 + i*30, fill=cores[i % len(cores)])
            canvas.create_text(640, 110 + i*30, text=f"{campus} ({qtd})", anchor="w")
            
            ang_inicial = ang_final

        canvas.create_text(x, y-r-30, text="Distribuição de Cursos por Campus", font=("Arial", 12, "bold"))

    def gerar_grafico_ampla_vs_cotas(self, canvas):
        canvas.delete("all")
        
        diffs = []
        
        for curso in self.cursos:
            ampla = [n.get_media() for n in curso.get_notas() if n.get_demanda() == "AC"]
            cotas = [n.get_media() for n in curso.get_notas() if n.get_demanda() != "AC"]
            
            if not ampla or not cotas:
                continue
                
            media_ampla = sum(ampla)/len(ampla)
            media_cotas = sum(cotas)/len(cotas)
            diffs.append((curso.get_nome(), media_ampla, media_cotas, abs(media_ampla-media_cotas)))

        diffs = sorted(diffs, key=lambda x: -x[3])[:10]
        
        if not diffs:
            canvas.create_text(200, 100, text="Sem dados")
            return

        largura = 800
        altura = 450
        margem = 80
        largura_barra = 30

        max_y = max([max(a, c) for n, a, c, d in diffs]) * 1.1
        min_y = min([min(a, c) for n, a, c, d in diffs]) * 0.9
        
        escala_y = (altura-margem*2)/(max_y-min_y)
        passo_x = (largura-margem*2)/len(diffs)

        for i, (nome, ampla, cotas, diff) in enumerate(diffs):
            x = margem + i*passo_x + passo_x/2

            y_ampla = altura-margem - (ampla-min_y)*escala_y
            y_cotas = altura-margem - (cotas-min_y)*escala_y

            canvas.create_rectangle(x-largura_barra/2, y_ampla, x, altura-margem, fill="blue", outline="blue")
            canvas.create_rectangle(x, y_cotas, x+largura_barra/2, altura-margem, fill="red", outline="red")

            # Nome do curso (abreviado)
            nome_abreviado = nome[:15] + "..." if len(nome) > 15 else nome
            canvas.create_text(x, altura-margem+15, text=nome_abreviado, font=("Arial", 8), angle=45, anchor="ne")

        # Legenda
        canvas.create_rectangle(600, 100, 620, 120, fill="blue")
        canvas.create_text(640, 110, text="Ampla Concorrência", anchor="w")
        canvas.create_rectangle(600, 140, 620, 160, fill="red")
        canvas.create_text(640, 150, text="Cotas", anchor="w")

        canvas.create_text(largura//2, 20, text="Ampla vs Cotas (Top 10 Diferenças)",
                           font=("Arial", 12, "bold"))