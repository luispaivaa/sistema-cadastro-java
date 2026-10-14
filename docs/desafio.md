# Desafio de Programação - Sistema de Cadastros

Desafio criado por Lucas Carrilho - [@devmagro](https://www.linkedin.com/in/karilho/)

- Twitter: [@devmagro](https://x.com/devmagro)
- Instagram: [@devmagro](https://instagram.com/devmagro)
- YouTube: [@devmagro](https://www.youtube.com/@devmagro)

## Introdução

O desafio propõe a criação de um Sistema de Adoção para Pets, aplicando conhecimentos de programação em uma aplicação de linha de comando.

## Objetivo

Criar um sistema de cadastro via CLI para que o responsável por um abrigo possa:

- Cadastrar um novo pet.
- Buscar dados de pets cadastrados.
- Excluir um pet cadastrado.
- Listar todos os pets cadastrados.
- Listar pets por critérios como idade, nome ou raça.

O sistema deve utilizar conceitos de Orientação a Objetos e boas práticas de programação.

## Conhecimentos propostos

- Orientação a Objetos (OO).
- Manipulação de arquivos e arrays.
- Java IO.
- Exceções.
- Boas práticas de código.
- Sistemas de arquivos.

## Requisitos do desafio

O desafio é organizado em etapas para implementar funcionalidades na aplicação CLI. A organização de entidades, serviços e utilitários fica a critério da implementação.

### Passo 1 - Formulário de perguntas

Criar `formulario.txt` com as perguntas essenciais:

1. Qual o nome e sobrenome do pet?
2. Qual o tipo do pet (Cachorro/Gato)?
3. Qual o sexo do animal?
4. Qual endereço e bairro que ele foi encontrado?
5. Qual a idade aproximada do pet?
6. Qual o peso aproximado do pet?
7. Qual a raça do pet?

A aplicação deve ler as perguntas do arquivo e exibi-las no terminal, em vez de manter o texto das perguntas diretamente no código.

### Passo 2 - Menu inicial

O menu deve oferecer as opções:

1. Cadastrar um novo pet.
2. Alterar os dados de um pet cadastrado.
3. Deletar um pet cadastrado.
4. Listar todos os pets cadastrados.
5. Listar pets por algum critério (idade, nome ou raça).
6. Sair.

O usuário escolhe uma opção pelo número. Entradas inválidas devem ser tratadas e o menu deve ser apresentado novamente.

### Passo 3 - Cadastro de pets

As respostas do formulário devem ser armazenadas em um objeto `Pet`.

Regras propostas:

1. Nome e sobrenome são obrigatórios.
2. Nome completo deve conter apenas letras de A a Z.
3. Tipo e sexo devem ser representados por `enum`.
4. O endereço deve solicitar número da casa, cidade e rua.
5. Idade e peso devem aceitar números com ponto ou vírgula decimal.
6. O peso permitido deve estar entre 0,5 kg e 60 kg.
7. A idade máxima deve ser de 20 anos.
8. Idades informadas em meses devem ser convertidas para frações de ano.
9. A raça não deve conter números nem caracteres especiais.
10. Campos opcionais sem resposta devem receber a constante `NAO_INFORMADO`, incluindo o número do endereço.

### Passo 4 - Armazenamento em arquivo

Cada pet deve ser salvo em um arquivo `.txt` dentro de `petsCadastrados/`, na raiz do projeto. O nome deve seguir o formato `AAAAMMDDTHHmm-NOMEESOBRENOME.TXT`, com nome e sobrenome em maiúsculas.

O arquivo deve conter as respostas, uma por linha, e o endereço deve ocupar uma única linha. Exemplo:

```text
1 - Florzinha da Silva
2 - Gato
3 - Femea
4 - Rua 2, 456, Seilandia
5 - 6 anos
6 - 5 kg
7 - Siames
```

### Passo 5 - Busca de pets

O enunciado propõe buscas por nome ou sobrenome, sexo, idade, peso, raça e endereço, com combinações de até dois critérios. O tipo do animal deve ser escolhido primeiro.

Os resultados devem ser exibidos em lista. A busca por texto deve aceitar partes do nome e ignorar diferenças entre maiúsculas/minúsculas e acentos.

Como opção extra, o desafio sugere busca por data de cadastro e destaque do termo encontrado.

### Passo 6 - Alteração de cadastro

O usuário deve localizar o pet, selecionar um item da lista e poder alterar os dados, exceto tipo e sexo.

### Passo 7 - Exclusão de cadastro

O usuário deve localizar e selecionar um pet, confirmar a exclusão com `SIM` ou `NÃO` e excluir apenas um cadastro por vez.

### Passo 8 - Encerramento

O usuário deve poder encerrar a aplicação pelo menu.

### Passo extra - Edição do formulário

Como funcionalidade opcional, o desafio propõe uma área para criar, alterar e excluir perguntas adicionais. As sete perguntas originais não podem ser alteradas nem excluídas. As perguntas extras devem manter numeração sequencial e suas respostas devem ser incluídas nos arquivos dos pets.

## Considerações finais do enunciado

O desafio recomenda evoluir a aplicação gradualmente, criar commits conforme as funcionalidades forem concluídas e documentar o projeto com instruções de execução.
