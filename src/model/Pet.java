    package model;

    import exceptions.IdadeInvalidaException;
    import exceptions.NomeInvalidoException;
    import exceptions.PesoInvalidoException;
    import exceptions.SobrenomeObrigatorioException;

    import java.io.BufferedReader;
    import java.io.BufferedWriter;
    import java.io.File;
    import java.io.FileReader;
    import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Scanner;

    public class Pet {
        String nomePet;
        String sobrenomePet;
        public static final String NAO_INFORMADO = "NAO_INFORMADO";
        TipoPet tipoPet;
        SexoPet sexo;
        String racaPet;
        Double pesoPet;
        Double idadePet;
        Endereco endereco = new Endereco();
        static List<Pet> listaPetCadastrados = new ArrayList<>();
        static Scanner input = new Scanner(System.in);
        static final String PASTA_PETS = "petsCadastrados";
        static final String ARQUIVO_FORMULARIO = "arquivos-de-texto/formulario.txt";


        public Pet() {
        }

    //    public model.Pet(String nomePet, String tipoPet, String sexo, double pesoPet, String racaPet, model.Endereco endereco) {
    //        this.nomePet = nomePet;
    //        this.tipoPet = tipoPet;
    //        this.sexo = sexo;
    //        this.pesoPet = pesoPet;
    //        this.racaPet = racaPet;
    //        this.endereco = endereco;
    //    }

        public static void showMenu(){
            System.out.println("\n\n--------BEM-VINDO AO SISTEMA DE CADASTRO---------\n");
            System.out.println("1 - Cadastrar um novo pet\n2 - Alterar os dados do pet cadastrado\n3 - Deletar um pet cadastrado\n4 - Listar todos os pets cadastrados\n5 - Listar pets por algum critério (idade, nome, raça)\n" +
                    "6 - Sair");
            System.out.println("\nEscolha uma opção: ");
            String opcaoEscolhida = input.nextLine();
            switch (opcaoEscolhida.trim()){
                case "1":
                    scanForms();
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    System.out.println(Pet.listaPetCadastrados);
                    break;
                case "5":
                    break;
                case "6":
                    break;
                default:
                    System.out.println("Opção inválida");
                    showMenu();
                    break;
            }
            
        }

        public static void scanForms() {
            File formularioPerguntas = new File(ARQUIVO_FORMULARIO);

            try(FileReader fileReader = new FileReader(formularioPerguntas);
                BufferedReader bufferedReader = new BufferedReader(fileReader);) {
                    String linha;
                    int indexPergunta = 1;
                    Pet novoPet = new Pet();
                    while ((linha = bufferedReader.readLine()) != null) {
                        linha = linha.trim();
                        while (true) {
                            try {
                                System.out.println(linha);
                                System.out.println("Resposta da pergunta " + indexPergunta + ": ");
                                String resposta = "";
                                if (indexPergunta != 4) {
                                    resposta = input.nextLine();
                                }
                                switch (indexPergunta) {
                                    case 1 -> {
                                        String regexApenasLetras = "^[A-Za-z]+(\\s+[A-Za-z]+)+$";
                                        String regexApenasNome = "^[A-Za-z]+$";
                                        if (resposta.trim().matches(regexApenasNome)) {
                                            throw new SobrenomeObrigatorioException("Informe nome e sobrenome.");
                                        }
                                        if (!resposta.trim().matches(regexApenasLetras)) {
                                            throw new NomeInvalidoException("Digite somente letras.");
                                        }

                                        String nomeCompleto = resposta.trim();
                                        String[] partes = resposta.trim().split("\\s+", 2);
                                        novoPet.nomePet = partes[0];
                                        novoPet.sobrenomePet = partes[1];
                                        /* valida nome; throw se inválido; atribui */
                                    }
                                    case 2 -> {
                                        String respostaUpper = resposta.trim().toUpperCase();
                                        try {
                                            novoPet.tipoPet = TipoPet.valueOf(respostaUpper);
                                        } catch (IllegalArgumentException e) {
                                            throw new IllegalArgumentException("Tipo inválido.");
                                        }
                                        /* model.TipoPet.valueOf(...); throw se inválido */
                                    }
                                    case 3 -> {
                                        try {
                                            novoPet.sexo = SexoPet.valueOf(resposta.trim().toUpperCase());
                                        } catch (IllegalArgumentException e) {
                                            throw new IllegalArgumentException("Sexo inválido. Tente novamente!");
                                        }

                                        /* model.SexoPet.valueOf(...) */
                                    }
                                    case 4 -> {
                                        Endereco respostaEndereco = novoPet.coletaEndereco(input);
                                        novoPet.endereco = respostaEndereco;
                                        /* endereço (vários campos) */
                                    }
                                    case 5 -> {
                                        Double idade = lerNumero(resposta);
                                        if (idade != null && idade > 20) {
                                            throw new IdadeInvalidaException("A idade deve ser no máximo 20 anos.");
                                        }
                                        novoPet.idadePet = idade; // null = não informado
                                        /* idade */
                                    }
                                    case 6 -> {
                                        Double peso = lerNumero(resposta);
                                        if (peso != null && (peso < 0.5 || peso > 60)) {
                                            throw new PesoInvalidoException("O peso deve estar entre 0.5 kg e 60 kg");
                                        }
                                        novoPet.pesoPet = peso;
                                        /* peso */
                                    }
                                    case 7 -> {
                                            String regexVariosNomes = "^[A-Za-z]+(\\s+[A-Za-z]+)+$";
                                            String regexUmNome = "^[A-Za-z]+$";

                                            String raca = resposta.trim();

                                            if (raca.isBlank()){
                                                novoPet.racaPet = NAO_INFORMADO;
                                            } else if (!raca.matches(regexUmNome) && !raca.matches(regexVariosNomes)) {
                                                throw new IllegalArgumentException("Digite uma raça válida");
                                            } else {
                                                novoPet.racaPet = raca;
                                            }
                                        /* raça */
                                    }
                                }
                                break; // só chega aqui se ninguém lançou
                            } catch (NomeInvalidoException
                                    | SobrenomeObrigatorioException
                                    | IllegalArgumentException
                                    | PesoInvalidoException
                                    | IdadeInvalidaException e) {
                                System.out.println("\n" + e.getMessage().toUpperCase() + "\n");
                            }
                        }
                        indexPergunta++;
                    }
                    addPets(novoPet);
                    salvarPetArquivo(novoPet);
                    finalCadastro();
                } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public Endereco coletaEndereco(Scanner input) {
            Endereco novoEndereco = new Endereco();
            System.out.println("Nome da Cidade - ");
            novoEndereco.nomeCidade = input.nextLine().trim();
            System.out.println("Nome do Bairro - ");
            novoEndereco.nomeBairro = input.nextLine().trim();
            System.out.println("Nome da Rua - ");
            novoEndereco.nomeRua = input.nextLine().trim();
            while (true) {
                System.out.println("Número da Casa - ");
                String numero = input.nextLine().trim();
                if (numero.isBlank()){
                    novoEndereco.numeroCasa = NAO_INFORMADO;
                    break;
                }
                if (numero.matches("[0-9]+")) {
                    novoEndereco.numeroCasa = numero;
                    break;
                }
                System.out.println("DIGITE APENAS NÚMEROS.\n");
            }
            return novoEndereco;
        }

        public static void showForms(){
            File formularioPerguntas = new File("/home/luispaiva/Documents/dev/devDojo/sistema-cadastro-java/arquivos-de-texto/formulario.txt");

            try(FileReader fileReader = new FileReader(formularioPerguntas);
                BufferedReader bufferedReader = new BufferedReader(fileReader);) {

                String linha;
                while ((linha = bufferedReader.readLine()) != null){
                    System.out.println(linha);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void finalCadastro(){
            while (true) {
                    System.out.println("\n\n----- CADASTRO FINALIZADO -----\n\n");
                    System.out.println("1 - MENU\n2 - CADASTRAR NOVO PET");
                    System.out.println("\nRESPOSTA -> ");
                    String resposta = input.nextLine().trim();
                    if (resposta.equals("1")) {
                        showMenu();
                        return;
                    }
                    if (resposta.equals("2")) {
                        scanForms();
                        return;
                    }
                System.out.println("DIGITE UMA RESPOSTA VÁLIDA.");
            }
        }

        public static Double lerNumero(String resposta){
            String r = resposta.trim();
            if (r.isBlank()) {
                return null;
            }
            if (!r.matches("\\d+([.,]\\d+)?")) {
                throw new IllegalArgumentException("Digite apenas números.");
            }
            return Double.parseDouble(r.replace(',' , '.'));
        }

        public static void addPets(Pet petCadastrado){
            Pet.listaPetCadastrados.add(petCadastrado);

        }
        
        public static void salvarPetArquivo(Pet pet) {
           String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
           String nomeArquivo = dataHora + "-" + (pet.nomePet + pet.sobrenomePet).replace(" ", "") + ".TXT";

           File pastaPets = new File(PASTA_PETS);
           pastaPets.mkdir(); 

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pastaPets, nomeArquivo)))) {
                
                writer.write("1 - " + pet.nomePet + " " + pet.sobrenomePet + "\n");
                writer.write("2 - " + pet.tipoPet + "\n");
                writer.write("3 - " + pet.sexo + "\n");
                writer.write("4 - " + pet.endereco.nomeRua + ", " + pet.endereco.numeroCasa + ", " + pet.endereco.nomeBairro + "\n");
                writer.write("5 - " + (pet.idadePet == null ? NAO_INFORMADO : pet.idadePet) + "\n");
                writer.write("6 - " + (pet.pesoPet == null ? NAO_INFORMADO : pet.pesoPet) + "\n");
                writer.write("7 - " + (pet.racaPet == null ? NAO_INFORMADO : pet.racaPet) + "\n");
        

            } catch (IOException e){
                System.out.println("Erro ao salvar o pet no arquivo: " + e.getMessage());
            }
        }
        @Override
        public String toString() {
            return "Pet{" +
                    "nomePet='" + nomePet + '\'' +
                    ", sobrenomePet='" + sobrenomePet + '\'' +
                    ", tipoPet=" + tipoPet +
                    ", sexo=" + sexo +
                    ", racaPet='" + racaPet + '\'' +
                    ", pesoPet=" + (pesoPet == null ? NAO_INFORMADO : pesoPet) +
                    ", idadePet=" + (idadePet == null ? NAO_INFORMADO : idadePet) +
                    ", endereco=" + endereco +
                    '}';
        }

    }