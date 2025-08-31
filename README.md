Tema: Análise exploratória dos dados do SISU da UFS

Discentes: Antonio Carlos Bispo Cunha e João Felipe Tunes Oliveira 

Link do repositório: https://github.com/ToniKrls/ProjetoPOO



-> Descrição do Projeto

Este projeto tem como objetivo realizar uma análise exploratória dos dados do SISU da UFS, permitindo a visualização de informações
importantes através de gráficos interativos.A aplicação foi desenvolvida em Java utilizando JavaFX para a construção da interface gráfica.
Com ela, o usuário pode carregar um arquivo CSV contendo os dados do SISU e visualizar diferentes estatísticas, como notas de corte por
modalidade de concorrência, distribuição por campus e comparações entre ampla concorrência e cotas.
Já a segunda linguagem escolhida foi Python, onde usamos o Tkinter para a construção da interface gráfica, gerando os mesmo gráficos da aplicação do projeto em Java.



-> Funcionalidades

A aplicação gera quatro tipos de gráficos principais:

Top 10 Notas de Corte (Ampla Concorrência):
Exibe um gráfico de linhas com os cursos com as maiores notas de corte para ampla concorrência.

Top 10 Notas de Corte (Cotas):
Exibe um gráfico de linhas com os cursos com as maiores notas de corte considerando apenas cotas.

Distribuição por Campus:
Gráfico de pizza mostrando a quantidade de cursos oferecidos em cada campus.

Comparação Ampla Concorrência x Cotas (Top 10 Diferenças):
Gráfico de barras comparando os 10 cursos em que a nota de corte entre ampla e cotas mais difere.


-> Python como segunda linguagem OO

A escolha do Python foi motivada por ser uma linguagem de programação orientada a objetos que, junto com a biblioteca Tkinter, permite criar programas com interface gráfica de forma similar a outras linguagens OO como Java (que foi a primeira linguagem do projeto). Dessa forma, a arquitetura do programa segue os princípios da programação orientada a objetos, organizando o código em classes especializadas.

A classe Nota encapsula as informações básicas de cada registro com ano, demanda, média e colocação, usando o duplo underscore para atributos privados. A classe Curso gerencia as informações dos cursos e faz uma composição com uma lista de objetos Nota. O projeto também inclui uma classe SisuService que carrega e processa dados de arquivos CSV, com tratamento de exceções para lidar com possíveis erros. A classe Main é responsável pela interface gráfica e herda de tk.Tk.

A implementação em Java e Python apresenta diferenças importantes. Em termos de sintaxe, Java exige declaração explícita de tipos e modificadores de acesso, enquanto Python é mais flexível. Java usa palavras-chave como 'private' e 'public' para encapsulamento, enquanto Python usa convenções como o duplo underscore.

Sobre as listas, há uma diferença fundamental: Python tem listas dinâmicas que podem misturar tipos de dados diferentes e são criadas de forma simples com colchetes. Java precisa usar classes como ArrayList e deve especificar o tipo de dados que a lista vai armazenar, sendo mais rígido mas também mais seguro em relação aos tipos de dados.

Em conclusão, ambas as linguagens permitem implementar programas orientados a objetos, mas com abordagens diferentes. Entretanto, mesmo com as diferenças, permite criar projetos similares e com o mesmo propósito, tornando o Python como uma boa escolha para o projeto.


-> Como Executar o Projeto JAVA

1- Clone o repositório:
   git clone https://github.com/ToniKrls/ProjetoPOO.git

2- Abra o projeto na sua IDE ou no prompt de comando

3- Certifique-se de que o JavaFX está configurado corretamente na sua IDE

4- Compile e execute a classe Main (que está em src/main/java)

5- Selecione um arquivo CSV válido quando a aplicação pedir (dentro da pasta "Resources" se encontram dois arquivos CSV para uso)


-> Como Executar o Projeto Python

1- Clone o repositório:
   git clone https://github.com/ToniKrls/ProjetoPOO.git

2- Abra o projeto na sua IDE ou no prompt de comando.

3- Compile e execute a classe Main (que está em src/main/python)

4- Aperte em "carregar CSV"

5- Selecione um arquivo CSV válido (dentro da pasta "Resources" se encontram dois arquivos CSV para uso).
