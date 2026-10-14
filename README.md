# Sistema de Cadastro de Pets

Aplicação Java de linha de comando para cadastrar e gerenciar pets encontrados. Este projeto é a resolução prática do [desafio de cadastro de pets](docs/desafio.md) e está em sua primeira versão funcional: um produto mínimo viável (MVP).

## Sobre o MVP

O objetivo desta versão é oferecer um fluxo utilizável para cadastrar pets, manter os dados entre execuções e realizar operações básicas de consulta e gerenciamento pelo terminal. Os cadastros são armazenados em arquivos de texto locais; não há banco de dados nem interface gráfica.

## Funcionalidades implementadas

- Exibir um menu de terminal para acessar as operações do sistema.
- Ler as perguntas do cadastro a partir de `arquivos-de-texto/formulario.txt`.
- Cadastrar pets com nome, sobrenome, tipo, sexo, endereço, idade, peso e raça.
- Representar tipo e sexo com enums e validar entradas do cadastro.
- Aplicar validações de nome, idade e peso, com exceções específicas para dados inválidos.
- Listar os pets cadastrados.
- Buscar pets por tipo e por idade, nome ou raça; buscas por nome e raça aceitam correspondência parcial e normalizam acentos e maiúsculas/minúsculas.
- Alterar dados do cadastro e excluir um pet após confirmação.
- Salvar cada cadastro em um arquivo `.TXT` em `petsCadastrados/` e carregar os arquivos disponíveis ao iniciar.
- Encapsular os atributos de `Pet` com getters e setters.

## Evolução da resolução

1. Modelagem das entidades `Pet` e `Endereco` e criação dos enums para tipo e sexo.
2. Construção do cadastro guiado pelo formulário externo.
3. Inclusão de validações e exceções para entradas inválidas.
4. Persistência dos dados em arquivos de texto e leitura dos cadastros existentes na inicialização.
5. Implementação dos fluxos de listagem, busca, alteração e exclusão pelo menu.
6. Encapsulamento dos dados de `Pet` com atributos privados e acessores.

## Estrutura do projeto

```text
.
├── arquivos-de-texto/
│   └── formulario.txt
├── docs/
│   └── desafio.md
├── petsCadastrados/
└── src/
	├── Main.java
	├── exceptions/
	└── model/
```

- `src/Main.java`: ponto de entrada; carrega os cadastros e inicia o menu.
- `src/model/`: classes e enums do domínio, incluindo `Pet`, `Endereco`, `TipoPet` e `SexoPet`.
- `src/exceptions/`: exceções usadas nas validações.
- `arquivos-de-texto/formulario.txt`: perguntas apresentadas durante o cadastro.
- `petsCadastrados/`: arquivos de texto persistidos para cada pet.
- `docs/desafio.md`: enunciado que orientou a implementação.

## Requisitos

- JDK instalado e disponível no terminal (`java` e `javac`).
- Execução a partir da raiz do projeto, pois os caminhos do formulário e dos dados são relativos ao diretório atual.

## Compilar e executar

Na raiz do repositório, execute:

```bash
mkdir -p out
javac -d out $(find src -name '*.java')
java -cp out Main
```

Os arquivos de cadastro ficam em `petsCadastrados/` e são carregados quando o programa inicia. Preserve essa pasta para manter os dados entre execuções.

## Escopo e próximas evoluções

Esta entrega prioriza o fluxo essencial do desafio. A busca disponível cobre tipo, idade, nome e raça; opções como busca por sexo, peso, endereço ou combinações de critérios ainda não fazem parte do MVP. O passo extra de administração das perguntas do formulário também não está implementado.

Possíveis evoluções incluem testes automatizados, separação da interface de terminal das regras de domínio, validação mais abrangente das operações e suporte às funcionalidades restantes do enunciado.
