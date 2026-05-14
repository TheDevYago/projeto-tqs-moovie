# 🎬  moovie - Sistema de Recomendação de Filmes

> *Sua próxima grande história, no tempo que você tem.*

O *moovie* é um sistema inteligente de recomendação de filmes que resolve o problema da "fadiga de decisão" nos streamings. Ao contrário de algoritmos genéricos, o moovie cruza a afinidade do seu perfil com o seu *tempo disponível no momento*, garantindo que a sugestão encaixe perfeitamente na sua rotina.

---

## Funcionalidades Coração

- *Algoritmo de Afinidade:* Cálculo de score baseado em pesos de género (70%) e adequação de tempo (30%).
- *Integração TMDB:* Consome dados reais de um catálogo de 100 filmes via API pública.
- *Modo Surpreenda-me:* Seleção aleatória inteligente dentro do conjunto de filmes que já passaram pelos seus filtros.
- *Filtros Rigorosos:* Remoção automática de filmes já assistidos, idiomas não aceites e classificações etárias inadequadas.

---

## Tecnologias Utilizadas

- *Linguagem:* Java 21+
- *Gestão de Dependências:* Maven
- *Testes:* JUnit 5 & Mockito
- *Cobertura:* JaCoCo
- *API Externa:* [The Movie Database (TMDB)](https://www.themoviedb.org/)
- *JSON Parsing:* Google Gson

---

## Como Rodar o Projeto

### Pré-requisitos
- JDK 21 ou superior instalado.
- Maven instalado e configurado no PATH.

### Configuração
1. Clone o repositório:
   ```bash
   git clone [https://github.com/teu-usuario/projeto-tqs-moovie.git](https://github.com/teu-usuario/projeto-tqs-moovie.git)
No ficheiro TmdbService.java, insira a sua API_KEY da TMDB.

Para rodar a aplicação principal:

```bash
mvn compile exec:java -Dexec.mainClass="br.com.moodie.Main"
```
##  Estratégia de Testes
O projeto segue rigorosos padrões de Testes de Qualidade de Software (TQS), divididos em:

- Testes Unitários: Validação isolada de lógica de negócio (Perfil, Filme, Calculadora).

- Testes de Integração: Pipeline completo utilizando Mockito para isolar a API externa e validar o fluxo de dados.

- Análise de Cobertura: Utilização do JaCoCo para garantir >80% de cobertura.

### Executando os Testes
Para rodar a suite completa:

```bash
mvn clean test
```
Dica para Windows: Se o seu nome de usuário possuir caracteres especiais (como apóstrofos), o projeto está configurado para utilizar um repositório local em C:/m2-repo para evitar erros de unbalanced quotes no terminal.

##  Arquitetura
- Diagrama de Classes
O sistema foi desenhado seguindo princípios de SOLID e Injeção de Dependências.

- Diagrama de Sequência (Recomendação)
Fluxo desde a requisição do utilizador até a entrega do ranking ordenado.

##  Cobertura de Código (JaCoCo)
Atualmente, o projeto mantém uma cobertura superior a 80%, garantindo que os fluxos críticos de filtragem e cálculo de score estão protegidos contra regressões.

(relatório index.html do JaCoCo)

## Integrantes
Este projeto foi desenvolvido para a disciplina de Testes e Qualidade de Software (UCSAL):

Ariely Figueiredo Floquet Paim

Felipe Fornazeli Rocha

Marcos Yago Rocha Vieira

© 2026 moovie Team.
