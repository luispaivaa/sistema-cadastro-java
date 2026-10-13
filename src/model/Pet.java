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
        File arquivoPet;
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
                    menuAlterarPetCadastrado();
                    break;
                case "3":
                    menuDeletarPetCadastrado();
                    break;
                case "4":
                    if (listaPetCadastrados.isEmpty()){
                        System.out.println("\n\nNenhum pet cadastrado.\n\n");
                        showMenu();
                    } else {
                        System.out.println("\n\n--- PETS CADASTRADOS ---\n\n");
                        mostrarPetsPorLinha(listaPetCadastrados);
                    }
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
           File arquvivo = new File(pastaPets, nomeArquivo);
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(arquvivo))) {
                
                writer.write("1 - " + pet.nomePet + " " + pet.sobrenomePet + "\n");
                writer.write("2 - " + pet.tipoPet + "\n");
                writer.write("3 - " + pet.sexo + "\n");
                writer.write("4 - " + pet.endereco.nomeCidade + ", " + pet.endereco.nomeRua + ", " + pet.endereco.numeroCasa + ", " + pet.endereco.nomeBairro + "\n");
                writer.write("5 - " + (pet.idadePet == null ? NAO_INFORMADO : pet.idadePet + " anos") + "\n");
                writer.write("6 - " + (pet.pesoPet == null ? NAO_INFORMADO : pet.pesoPet + " kg") + "\n");
                writer.write("7 - " + (pet.racaPet == null ? NAO_INFORMADO : pet.racaPet) + "\n");
                pet.arquivoPet = arquvivo;

            } catch (IOException e){
                System.out.println("Erro ao salvar o pet no arquivo: " + e.getMessage());
            }
        }

        public static void exibirMenuBuscaPets(){
            System.out.println("\n\n--------BEM-VINDO AO MENU DE BUSCA DE PETS---------\n");
            System.out.println("Informe o tipo de PET que deseja buscar (CACHORRO ou GATO): ");
            String tipoPet = input.nextLine();
            String tipoPetFormatado = tipoPet.trim().toUpperCase();
            if (!tipoPetFormatado.equals("CACHORRO") && !tipoPetFormatado.equals("GATO")) {
                System.out.println("Tipo de PET inválido. Tente novamente.");
                exibirMenuBuscaPets();
                return;
            }

            System.out.println("1 - Buscar por idade\n2 - Buscar por nome\n3 - Buscar por raça\n4 - Voltar ao menu principal\n");
            System.out.println("\nEscolha uma opção: ");
            String opcaoEscolhida = input.nextLine();
            switch (opcaoEscolhida.trim()){
                case "1":
                    buscarPetsPorIdade(TipoPet.valueOf(tipoPetFormatado));
                    break;
                case "2":
                    buscarPetsPorNome(TipoPet.valueOf(tipoPetFormatado));
                    break;
                case "3":
                    buscarPetsPorRaca(TipoPet.valueOf(tipoPetFormatado));
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

        public static List<Pet> buscarPetsPorIdade(TipoPet tipoPet){
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
                mostrarPetsPorLinha(petsEncontrados);
            }
            return petsEncontrados;
        }

       
                
        

        public static List<Pet> buscarPetsPorNome(TipoPet tipoPet){
            System.out.println("INFORME O NOME DO SEU PET: ");
            String resposta = input.nextLine();
            String respostaFormatada = normalizarTexto(resposta.trim());
            List<Pet> petsEncontradosNome = new ArrayList<>();

            for (Pet petCadastrado : listaPetCadastrados) {
                String nomeCompleto = petCadastrado.nomePet + " " + petCadastrado.sobrenomePet;
                String nomeCompletoFormatado = normalizarTexto(nomeCompleto.trim());
                if (petCadastrado.tipoPet == tipoPet && (nomeCompletoFormatado.contains(respostaFormatada))) {
                    petsEncontradosNome.add(petCadastrado);
                }
            }
            if (petsEncontradosNome.isEmpty()){
                System.out.println("\n\nNenhum pet cadastrado com esse nome.\n\n");
                exibirMenuBuscaPets();
            } else {
                System.out.println("\n\n--- PETS ENCONTRADOS ---\n\n");
                mostrarPetsPorLinha(petsEncontradosNome);
            }
            return petsEncontradosNome;
        }

        public static List<Pet> buscarPetsPorRaca(TipoPet tipoPet){
            System.out.println("\n\nINFORME A RAÇA DO SEU PET: \n");
            String resposta = input.nextLine();
            String respostaFormatada = normalizarTexto(resposta.trim());
            List<Pet> petsEncontradosRaca = new ArrayList<>();

            for (Pet petCadastrado : listaPetCadastrados) {
                String racaFormatada = normalizarTexto(petCadastrado.racaPet.trim());
                if (petCadastrado.tipoPet == tipoPet && racaFormatada.contains(respostaFormatada)){
                    petsEncontradosRaca.add(petCadastrado);
                }
            }
            if (petsEncontradosRaca.isEmpty()){
                System.out.println("Nenhum pet com essa raça foi cadastrado.");
                exibirMenuBuscaPets();
                } else {
                    System.out.println("\n\n--- PETS ENCONTRADOS ---\n\n");
                    mostrarPetsPorLinha(petsEncontradosRaca);
                }

                return petsEncontradosRaca;
            }

            public static String normalizarTexto(String texto){
                String semAcentos = Normalizer.normalize(
                    texto == null ? "" : texto, Normalizer.Form.NFD
                ).replaceAll("\\p{M}", "");
                return semAcentos.toLowerCase(Locale.ROOT);
            }
            
            public static void mostrarPetsPorLinha(List<Pet> pets) {
                for (int i = 0; i < pets.size(); i++) {
                    Pet pet = pets.get(i);
                    if (pet == null) {
                        System.out.println((i + 1) + ". " + NAO_INFORMADO);
                        continue;
                    }

                    String nomeCompleto = Objects.toString(pet.nomePet, NAO_INFORMADO);
                    if (pet.sobrenomePet != null && !pet.sobrenomePet.isBlank()) {
                        nomeCompleto += " " + pet.sobrenomePet;
                    }

                    String enderecoFormatado = pet.endereco == null
                            ? NAO_INFORMADO
                            : String.join(", ",
                                    Objects.toString(pet.endereco.nomeRua, NAO_INFORMADO),
                                    Objects.toString(pet.endereco.numeroCasa, NAO_INFORMADO),
                                    Objects.toString(pet.endereco.nomeBairro, NAO_INFORMADO),
                                    Objects.toString(pet.endereco.nomeCidade, NAO_INFORMADO));
                    String idade = pet.idadePet == null
                            ? NAO_INFORMADO
                            : pet.idadePet + " anos";
                    String peso = pet.pesoPet == null
                            ? NAO_INFORMADO
                            : pet.pesoPet + " kg";

                    System.out.println((i + 1) + ". " + nomeCompleto
                            + " - " + Objects.toString(pet.tipoPet, NAO_INFORMADO)
                            + " - " + Objects.toString(pet.sexo, NAO_INFORMADO)
                            + " - " + enderecoFormatado
                            + " - " + idade
                            + " - " + peso
                            + " - " + Objects.toString(pet.racaPet, NAO_INFORMADO));
                }
            }

            public static void menuAlterarPetCadastrado() {
            System.out.println("\n\n--------BEM-VINDO AO MENU DE BUSCA DE PETS---------\n");
            System.out.println("Informe o tipo de PET que deseja buscar (CACHORRO ou GATO): ");
            String tipoPet = input.nextLine();
            String tipoPetFormatado = tipoPet.trim().toUpperCase();
            if (!tipoPetFormatado.equals("CACHORRO") && !tipoPetFormatado.equals("GATO")) {
                System.out.println("Tipo de PET inválido. Tente novamente.");
                exibirMenuBuscaPets();
                return;
            }

            System.out.println("1 - Buscar por idade\n2 - Buscar por nome\n3 - Buscar por raça\n4 - Voltar ao menu principal\n");
            System.out.println("\nEscolha uma opção: ");
            String opcaoEscolhida = input.nextLine();
            switch (opcaoEscolhida.trim()){
                case "1":
                    buscarPetsPorIdade(TipoPet.valueOf(tipoPetFormatado));
                    if (buscarPetsPorIdade(TipoPet.valueOf(tipoPetFormatado)).isEmpty()) {
                        System.out.println("Nenhum pet encontrado com essa idade.");
                        exibirMenuBuscaPets();
                    } else {
                        System.out.println("Qual pet deseja alterar? Informe o número correspondente: ");
                        int numeroPet = Integer.parseInt(input.nextLine());
                        if (numeroPet < 1 || numeroPet > buscarPetsPorIdade(TipoPet.valueOf(tipoPetFormatado)).size()) {
                            System.out.println("Número inválido.");
                            exibirMenuBuscaPets();
                        } else {
                            Pet petSelecionado = buscarPetsPorIdade(TipoPet.valueOf(tipoPetFormatado)).get(numeroPet - 1);
                            System.out.println("Pet selecionado: " + petSelecionado.nomePet + " " + petSelecionado.sobrenomePet);
                            alterarPetCadastrado(petSelecionado);
                            }
                        }
                    break;
                case "2":
                    buscarPetsPorNome(TipoPet.valueOf(tipoPetFormatado));
                    if (buscarPetsPorNome(TipoPet.valueOf(tipoPetFormatado)).isEmpty()) {
                        System.out.println("Nenhum pet encontrado com esse nome.");
                        exibirMenuBuscaPets();
                    } else {
                        System.out.println("Qual pet deseja alterar? Informe o número correspondente: ");
                        int numeroPet = Integer.parseInt(input.nextLine());
                        if (numeroPet < 1 || numeroPet > buscarPetsPorNome(TipoPet.valueOf(tipoPetFormatado)).size()) {
                            System.out.println("Número inválido.");
                            exibirMenuBuscaPets();
                        } else {
                            Pet petSelecionado = buscarPetsPorNome(TipoPet.valueOf(tipoPetFormatado)).get(numeroPet - 1);
                            System.out.println("Pet selecionado: " + petSelecionado.nomePet + " " + petSelecionado.sobrenomePet);
                            alterarPetCadastrado(petSelecionado);
                            }
                        }
                    break;
                case "3":
                    buscarPetsPorRaca(TipoPet.valueOf(tipoPetFormatado));
                    if (buscarPetsPorRaca(TipoPet.valueOf(tipoPetFormatado)).isEmpty()) {
                        System.out.println("Nenhum pet encontrado com essa raça.");
                        exibirMenuBuscaPets();
                    } else {
                        System.out.println("Qual pet deseja alterar? Informe o número correspondente: ");
                        int numeroPet = Integer.parseInt(input.nextLine());
                        if (numeroPet < 1 || numeroPet > buscarPetsPorRaca(TipoPet.valueOf(tipoPetFormatado)).size()) {
                            System.out.println("Número inválido.");
                            exibirMenuBuscaPets();
                        } else {
                            Pet petSelecionado = buscarPetsPorRaca(TipoPet.valueOf(tipoPetFormatado)).get(numeroPet - 1);
                            System.out.println("Pet selecionado: " + petSelecionado.nomePet + " " + petSelecionado.sobrenomePet);
                            alterarPetCadastrado(petSelecionado);
                            }
                        }
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

            public static void alterarPetCadastrado(Pet pet) {
                System.out.println("Qual campo deseja alterar?");
                System.out.println("1 - Nome\n2 - Sobrenome\n3 - Endereço\n4 - Idade\n5 - Peso\n6 - Raça");
                String opcaoEscolhida = input.nextLine();
                switch (opcaoEscolhida.trim()) {
                    case "1":
                        System.out.println("Informe o novo nome: ");
                        String novoNome = input.nextLine();
                        pet.nomePet = novoNome;
                        break;
                    case "2":
                        System.out.println("Informe o novo sobrenome: ");
                        String novoSobrenome = input.nextLine();
                        pet.sobrenomePet = novoSobrenome;
                        break;
                    case "3":
                        Endereco novoEndereco = pet.coletaEndereco(input);
                        pet.endereco = novoEndereco;
                        break;
                    case "4":
                        System.out.println("Informe a nova idade: ");
                        Double novaIdade = lerNumero(input.nextLine());
                        pet.idadePet = novaIdade;
                        break;
                    case "5":
                        System.out.println("Informe o novo peso: ");
                        Double novoPeso = lerNumero(input.nextLine());
                        pet.pesoPet = novoPeso;
                        break;
                    case "6":
                        System.out.println("Informe a nova raça: ");
                        String novaRaca = input.nextLine();
                        pet.racaPet = novaRaca;
                        break;
                    default:
                        System.out.println("Opção inválida");
                        alterarPetCadastrado(pet);
                        break;
                }
            }

            public static void menuDeletarPetCadastrado(){
                System.out.println("\n\n--------BEM-VINDO AO MENU DE BUSCA DE PETS---------\n");
                System.out.println("Informe o tipo de PET que deseja buscar (CACHORRO ou GATO): ");
                String tipoPet = input.nextLine();
                String tipoPetFormatado = tipoPet.trim().toUpperCase();
                if (!tipoPetFormatado.equals("CACHORRO") && !tipoPetFormatado.equals("GATO")) {
                    System.out.println("Tipo de PET inválido. Tente novamente.");
                    exibirMenuBuscaPets();
                    return;
                }

                System.out.println("1 - Buscar por idade\n2 - Buscar por nome\n3 - Buscar por raça\n4 - Voltar ao menu principal\n");
                System.out.println("\nEscolha uma opção: ");
                String opcaoEscolhida = input.nextLine();
                switch (opcaoEscolhida.trim()){
                    case "1":
                        buscarPetsPorIdade(TipoPet.valueOf(tipoPetFormatado));
                        if (buscarPetsPorIdade(TipoPet.valueOf(tipoPetFormatado)).isEmpty()) {
                            System.out.println("Nenhum pet encontrado com essa idade.");
                            exibirMenuBuscaPets();
                        } else {
                            System.out.println("Qual pet deseja deletar? Informe o número correspondente: ");
                            int numeroPet = Integer.parseInt(input.nextLine());
                            if (numeroPet < 1 || numeroPet > buscarPetsPorIdade(TipoPet.valueOf(tipoPetFormatado)).size()) {
                                System.out.println("Número inválido.");
                                exibirMenuBuscaPets();
                            } else {
                                Pet petSelecionado = buscarPetsPorIdade(TipoPet.valueOf(tipoPetFormatado)).get(numeroPet - 1);
                                System.out.println("Pet selecionado: " + petSelecionado.nomePet + " " + petSelecionado.sobrenomePet);
                                deletarPetSelecionado(petSelecionado);
                            }
                        }
                        break;
                    case "2":
                        buscarPetsPorNome(TipoPet.valueOf(tipoPetFormatado));
                        if (buscarPetsPorNome(TipoPet.valueOf(tipoPetFormatado)).isEmpty()) {
                            System.out.println("Nenhum pet encontrado com esse nome.");
                            exibirMenuBuscaPets();
                        } else {
                            System.out.println("Qual pet deseja deletar? Informe o número correspondente: ");
                            int numeroPet = Integer.parseInt(input.nextLine());
                            if (numeroPet < 1 || numeroPet > buscarPetsPorNome(TipoPet.valueOf(tipoPetFormatado)).size()) {
                                System.out.println("Número inválido.");
                                exibirMenuBuscaPets();
                            } else {
                                Pet petSelecionado = buscarPetsPorNome(TipoPet.valueOf(tipoPetFormatado)).get(numeroPet - 1);
                                System.out.println("Pet selecionado: " + petSelecionado.nomePet + " " + petSelecionado.sobrenomePet);
                                deletarPetSelecionado(petSelecionado);
                            }
                        }
                        break;
                    case "3":
                        buscarPetsPorRaca(TipoPet.valueOf(tipoPetFormatado));
                        if (buscarPetsPorRaca(TipoPet.valueOf(tipoPetFormatado)).isEmpty()) {
                            System.out.println("Nenhum pet encontrado com essa raça.");
                            exibirMenuBuscaPets();
                        } else {
                            System.out.println("Qual pet deseja deletar? Informe o número correspondente: ");
                            int numeroPet = Integer.parseInt(input.nextLine());
                            if (numeroPet < 1 || numeroPet > buscarPetsPorRaca(TipoPet.valueOf(tipoPetFormatado)).size()) {
                                System.out.println("Número inválido.");
                                exibirMenuBuscaPets();
                            } else {
                                Pet petSelecionado = buscarPetsPorRaca(TipoPet.valueOf(tipoPetFormatado)).get(numeroPet - 1);
                                System.out.println("Pet selecionado: " + petSelecionado.nomePet + " " + petSelecionado.sobrenomePet);
                                deletarPetSelecionado(petSelecionado);
                            }
                        }
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

            public static void deletarPetSelecionado(Pet pet) {
                if (pet == null) {
                    System.out.println("Nenhum pet foi selecionado.");
                    showMenu();
                    return;
                }
                    while (true) {
                        System.out.println("\n\nTem certeza que deseja DELETAR esse Pet da lista ?\n\n");
                        System.out.println("[SIM] Confirmar\n[NÃO] Deletar ");
                        String respostaDelete = input.nextLine().trim();

                        if (respostaDelete.trim().equalsIgnoreCase("SIM")) {
                            if (!listaPetCadastrados.contains(pet)){
                                System.out.println("O pet selecionado não está na lista.");
                                showMenu();
                                return;
                            } if(pet.arquivoPet == null || !pet.arquivoPet.exists()){
                                System.out.println("O arquivo do pet não foi encontrado.");
                                showMenu();
                                return;
                            } if (listaPetCadastrados.contains(pet) && pet.arquivoPet.exists()) {
                                listaPetCadastrados.remove(pet);
                                boolean deletado = pet.arquivoPet.delete();
                                if (deletado) {
                                    System.out.println("\n\nPet deletado com sucesso.\n\n");
                                    showMenu();
                                    return;
                                } else {
                                    System.out.println("Não foi possível deletar o arquivo do pet.");
                                    showMenu();
                                    return;
                                }
                            }
                        } else {
                                System.out.println("O pet selecionado não está na lista.");
                            }
                        if (respostaDelete.equalsIgnoreCase("NÃO") || respostaDelete.equalsIgnoreCase("NAO")){
                            System.out.println("\n\nPet NÃO deletado.\n\n");
                            showMenu();
                            return;
                          } 
                        }
                    }

        public static void carregarPetsDoArquivo(){
            File pasta = new File(PASTA_PETS);

            if (!pasta.exists() || !pasta.isDirectory()){
                System.out.println("O diretório dos Pets Cadastrados não existe.");
                return;
            }

            File[] arquivos = pasta.listFiles((dir, name) -> name.toUpperCase().endsWith(".TXT"));
            if (arquivos == null){
                System.out.println("Não existem arquivos de pets cadastrados no diretório \"petsCadastrados\".");
                return;
            }

            Arrays.sort(arquivos);

            for (File arquivo : arquivos) {
                try {
                    // lerPetDoArquivo
                } catch (RuntimeException e){
                    System.out.println(e.getMessage());
                }
            }
        }

        public static void lerPetsDoArquivo(File arquivo) throws IOException{
            Pet pet = new Pet();
            pet.arquivoPet = arquivo;

            try (BufferedReader bf = new BufferedReader(new FileReader(arquivo))) {
                String linha;
                while ((linha = bf.readLine()) != null) {
                    linha = linha.trim();
                    if (linha.isEmpty()){
                        continue;
                    }

                    String[] partesDaLinha = linha.split(" - ", 2);
                    int indexCampo = Integer.parseInt(partesDaLinha[0].trim());
                    String campoValue = partesDaLinha[1].trim();

                    switch (indexCampo) {
                        case 1:
                            String[] nome = campoValue.split("\\s");
                            pet.nomePet = nome[0];
                            pet.sobrenomePet = nome.length > 1 ? nome[1] : "";
                        case 2:
                            pet.tipoPet = TipoPet.valueOf(campoValue);
                        case 3:
                            pet.sexo = SexoPet.valueOf(campoValue);
                        case 4:
                           pet.endereco = lerEnderecoDoArquivo(campoValue);
                        case 5 :
                            pet.idadePet = lerNumeroDoArquivo(campoValue, " anos");
                        case 6:
                            pet.idadePet = lerNumeroDoArquivo(campoValue, " kg");
                        case 7:
                            pet.racaPet = campoValue.trim();

                    }
                }
            }
        }

        private static Endereco lerEnderecoDoArquivo(String value){
            Endereco endereco = new Endereco();
            String[] partesDoEndereco = value.split(",");
            if (partesDoEndereco.length == 4){
                endereco.nomeCidade = partesDoEndereco[0].trim();
                endereco.nomeRua = partesDoEndereco[1].trim();
                endereco.numeroCasa = partesDoEndereco[2].trim();
                endereco.nomeBairro = partesDoEndereco[3].trim();
            } else {
                endereco.nomeCidade = value.trim();
            }
            return endereco;
        }

        private static Double lerNumeroDoArquivo(String value, String suffix) {
            if (value.equals(NAO_INFORMADO)){
                return null;
            }
            return Double.parseDouble(value.replace(suffix, "").trim());
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