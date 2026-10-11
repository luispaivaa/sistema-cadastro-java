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
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.text.Normalizer;
    import java.util.*;


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


        public Pet(String nomePet, String sobrenomePet, TipoPet tipoPet, SexoPet sexo, String racaPet, Double pesoPet, Double idadePet, Endereco endereco) {
            this.nomePet = nomePet;
            this.sobrenomePet = sobrenomePet;
            this.tipoPet = tipoPet;
            this.sexo = sexo;
            this.racaPet = racaPet;
            this.pesoPet = pesoPet;
            this.idadePet = idadePet;
            this.endereco = endereco;
        }

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
                    exibirMenuBuscaPets();
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
           String nomeArquivo = dataHora + "-" + (pet.nomePet + pet.sobrenomePet).replace(" ", "").toUpperCase() + ".TXT";

           File pastaPets = new File(PASTA_PETS);
           pastaPets.mkdir(); 

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pastaPets, nomeArquivo)))) {
                
                writer.write("1 - " + pet.nomePet + " " + pet.sobrenomePet + "\n");
                writer.write("2 - " + pet.tipoPet + "\n");
                writer.write("3 - " + pet.sexo + "\n");
                writer.write("4 - " + pet.endereco.nomeRua + ", " + pet.endereco.numeroCasa + ", " + pet.endereco.nomeBairro + "\n");
                writer.write("5 - " + (pet.idadePet == null ? NAO_INFORMADO : pet.idadePet + " anos") + "\n");
                writer.write("6 - " + (pet.pesoPet == null ? NAO_INFORMADO : pet.pesoPet + " kg") + "\n");
                writer.write("7 - " + (pet.racaPet == null ? NAO_INFORMADO : pet.racaPet) + "\n");
        

            } catch (IOException e){
                System.out.println("Erro ao salvar o pet no arquivo: " + e.getMessage());
            }
        }

        public static void exibirMenuBuscaPets(){
            System.out.println("\n\n--------BEM-VINDO AO MENU DE BUSCA DE PETS---------\n");
            System.out.println("Informe o tipo de PET que deseja buscar (CACHORRO ou GATO): ");
            String tipoPet = input.nextLine().trim().toUpperCase();
            if (!tipoPet.equals("CACHORRO") || !tipoPet.equals("GATO")) {
                System.out.println("Tipo de PET inválido. Tente novamente.");
                exibirMenuBuscaPets();
                return;
            }

            System.out.println("1 - Buscar por idade\n2 - Buscar por nome\n3 - Buscar por raça\n4 - Voltar ao menu principal\n");
            System.out.println("\nEscolha uma opção: ");
            String opcaoEscolhida = input.nextLine();
            switch (opcaoEscolhida.trim()){
                case "1":
                    buscarPetsPorIdade(TipoPet.valueOf(tipoPet));
                    break;
                case "2":
                    buscarPetsPorNome(TipoPet.valueOf(tipoPet));
                    break;
                case "3":
                    buscarPetsPorRaca(TipoPet.valueOf(tipoPet));
                    break;
                case "4":
                    showMenu();
                    break;
                default:
                    System.out.println("Opção inválida");
                    exibirMenuBuscaPets();
                    break;
            }
        }

        public static void buscarPetsPorIdade(TipoPet tipoPet){
            System.out.println("INFORME A IDADE DE SEU PET: ");
            String resposta = input.nextLine();
            Double idade = lerNumero(resposta);
            List<Pet> petsEncontrados = new ArrayList<>();
            for(Pet pet : listaPetCadastrados){
                if (pet.tipoPet == tipoPet && Objects.equals(pet.idadePet, idade)){
                    petsEncontrados.add(pet);
                }
            }
            if (petsEncontrados.isEmpty()){
                System.out.println("\n\nNenhum pet cadastrado com essa idade.\n\n");
                exibirMenuBuscaPets();
            } else {
                System.out.println("\n\n--- PETS ENCONTRADOS ---\n\n");
                int indice = 1;
                for (Pet petsEncontrado : petsEncontrados) {
                    System.out.println(indice + " - " + petsEncontrado.nomePet.toUpperCase());
                    indice++;
                }
            }
        }

        public static void buscarPetsPorNome(TipoPet tipoPet){
            System.out.println("INFORME O NOME DO SEU PET: ");
            String resposta = input.nextLine();
            String respostaFormatada = resposta.trim().toLowerCase();
            List<Pet> petsEncontradosNome = new ArrayList<>();
            for (Pet petCadastrado : listaPetCadastrados) {
                if (petCadastrado.tipoPet == tipoPet && (petCadastrado.nomePet.toLowerCase().contains(respostaFormatada) || petCadastrado.sobrenomePet.toLowerCase().contains(respostaFormatada))) {
                    petsEncontradosNome.add(petCadastrado);
                }
            }
            if (petsEncontradosNome.isEmpty()){
                System.out.println("\n\nNenhum pet cadastrado com esse nome.\n\n");
                exibirMenuBuscaPets();
            } else {
                System.out.println("\n\n--- PETS ENCONTRADOS ---\n\n");
                int indice = 1;
                for (Pet petsEncontrado : petsEncontradosNome) {
                    System.out.println(indice + " - " + petsEncontrado.nomePet.toUpperCase() + " " + petsEncontrado.sobrenomePet.toUpperCase());
                    indice++;
                }
            }
        }

        public static void buscarPetsPorRaca(TipoPet tipoPet){
            System.out.println("\n\nINFORME A RAÇA DO SEU PET: \n");
            String resposta = input.nextLine();
            String respostaFormatada = resposta.trim().toUpperCase();
            List<Pet> petsEncontradosRaca = new ArrayList<>();
            for (Pet petCadastrado : listaPetCadastrados) {
                if (petCadastrado.tipoPet == tipoPet && petCadastrado.racaPet.toUpperCase().contains(respostaFormatada)){
                    petsEncontradosRaca.add(petCadastrado);
                }
            }
            if (petsEncontradosRaca.isEmpty()){
                System.out.println("Nenhum pet com essa raça foi cadastrado.");
                exibirMenuBuscaPets();
                } else {
                    System.out.println("\n\n--- PETS ENCONTRADOS ---\n\n");
                    int indice = 1;
                    for (Pet petsEncontrado : petsEncontradosRaca) {
                        System.out.println(indice + " - " + petsEncontrado.nomePet.toUpperCase() + " " + petsEncontrado.sobrenomePet.toUpperCase());
                        indice++;
                    }
                }
            }

            public static String normalizarTexto(String texto){
                String semAcentos = Normalizer.normalize(
                    texto == null ? "" : texto, Normalizer.Form.NFD
                ).replaceAll("\\p{M}", "");
                return semAcentos.toLowerCase(Locale.ROOT);
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