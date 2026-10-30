package repository;

import exceptions.IdadeInvalidaException;
import exceptions.NomeInvalidoException;
import exceptions.PesoInvalidoException;
import exceptions.SobrenomeObrigatorioException;
import menu.MenuPet;
import model.Endereco;
import model.Pet;
import model.SexoPet;
import model.TipoPet;
import service.PetAddressService;
import service.PetService;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class PetRepository {
    private static final String ARQUIVO_FORMULARIO = "arquivos-de-texto/formulario.txt";
    private static final String NAO_INFORMADO = "NAO_INFORMADO";
    private static final String PASTA_PETS = "petsCadastrados";
    public static Scanner input = new Scanner(System.in);
    private static PetAddressService petAddressService = new PetAddressService();
    private static PetService petService = new PetService();


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
                Pet pet = lerPetsDoArquivo(arquivo);
                Pet.listaPetCadastrados.add(pet);
            } catch (IOException e){
                System.out.println("Não foi possível ler o arquivo: " + arquivo.getName());
            }
        }
    }
    private static Pet lerPetsDoArquivo(File arquivo) throws IOException{
        Pet pet = new Pet();
        pet.setArquivoPet(arquivo);

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
                    case 1 -> {
                        String[] nome = campoValue.split("\\s+", 2);
                        pet.setNomePet(nome[0]);
                        pet.setSobrenomePet(nome.length > 1 ? nome[1] : "");
                    }
                    case 2 -> pet.setTipoPet(TipoPet.valueOf(campoValue));
                    case 3 -> pet.setSexo(SexoPet.valueOf(campoValue));
                    case 4 -> pet.setEndereco(petAddressService.lerEnderecoDoArquivo(campoValue));
                    case 5 -> pet.setIdadePet(petService.lerNumeroDoArquivo(campoValue, " anos"));
                    case 6 -> pet.setPesoPet(petService.lerNumeroDoArquivo(campoValue, " kg"));
                    case 7 -> pet.setRacaPet(campoValue);
                    default -> throw new IOException("Campo desconhecido no arquivo: " + indexCampo);
                }
            }
        }
        return pet;
    }

    public void scanForms() {
        File formularioPerguntas = new File(ARQUIVO_FORMULARIO);

        try(FileReader fileReader = new FileReader(formularioPerguntas);
            BufferedReader bufferedReader = new BufferedReader(fileReader);) {
            PetService petService = new PetService();
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
                                novoPet.setNomePet(partes[0]);
                                novoPet.setNomePet(partes[1]);
                                /* valida nome; throw se inválido; atribui */
                            }
                            case 2 -> {
                                String respostaUpper = resposta.trim().toUpperCase();
                                try {
                                    novoPet.setTipoPet(TipoPet.valueOf(respostaUpper));
                                } catch (IllegalArgumentException e) {
                                    throw new IllegalArgumentException("Tipo inválido.");
                                }
                                /* model.TipoPet.valueOf(...); throw se inválido */
                            }
                            case 3 -> {
                                try {
                                    novoPet.setSexo(SexoPet.valueOf(resposta.trim().toUpperCase()));
                                } catch (IllegalArgumentException e) {
                                    throw new IllegalArgumentException("Sexo inválido. Tente novamente!");
                                }

                                /* model.SexoPet.valueOf(...) */
                            }
                            case 4 -> {
                                PetAddressService petAddressService = new PetAddressService();
                                Endereco respostaEndereco = petAddressService.coletaEndereco(input);
                                novoPet.setEndereco(respostaEndereco);
                                /* endereço (vários campos) */
                            }
                            case 5 -> {
                                Double idade = petService.lerNumero(resposta);
                                if (idade != null && idade > 20) {
                                    throw new IdadeInvalidaException("A idade deve ser no máximo 20 anos.");
                                }
                                novoPet.setIdadePet(idade);  // null = não informado
                                /* idade */
                            }
                            case 6 -> {
                                Double peso = petService.lerNumero(resposta);
                                if (peso != null && (peso < 0.5 || peso > 60)) {
                                    throw new PesoInvalidoException("O peso deve estar entre 0.5 kg e 60 kg");
                                }
                                novoPet.setPesoPet(peso);
                                /* peso */
                            }
                            case 7 -> {
                                String regexVariosNomes = "^[A-Za-z]+(\\s+[A-Za-z]+)+$";
                                String regexUmNome = "^[A-Za-z]+$";

                                String raca = resposta.trim();

                                if (raca.isBlank()){
                                    novoPet.setRacaPet(NAO_INFORMADO);
                                } else if (!raca.matches(regexUmNome) && !raca.matches(regexVariosNomes)) {
                                    throw new IllegalArgumentException("Digite uma raça válida");
                                } else {
                                    novoPet.setRacaPet(raca);
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
            Pet.addPets(novoPet);
            salvarPetArquivo(novoPet);
            MenuPet.finalCadastro();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void mostrarPetsPorLinha(List<Pet> pets) {
        for (int i = 0; i < pets.size(); i++) {
            Pet pet = pets.get(i);
            if (pet == null) {
                System.out.println((i + 1) + ". " + NAO_INFORMADO);
                continue;
            }

            String nomeCompleto = Objects.toString(pet.getNomePet(), NAO_INFORMADO);
            if (pet.getSobrenomePet() != null && !pet.getSobrenomePet().isBlank()) {
                nomeCompleto += " " + pet.getSobrenomePet();
            }

            String enderecoFormatado = pet.getEndereco() == null
                    ? NAO_INFORMADO
                    : String.join(", ",
                    Objects.toString(pet.getEndereco().getNomeCidade(), NAO_INFORMADO),
                    Objects.toString(pet.getEndereco().getNomeRua(), NAO_INFORMADO),
                    Objects.toString(pet.getEndereco().getNumeroCasa(), NAO_INFORMADO),
                    Objects.toString(pet.getEndereco().getNomeBairro(), NAO_INFORMADO));
            String idade = pet.getIdadePet() == null
                    ? NAO_INFORMADO
                    : pet.getIdadePet() + " anos";
            String peso = pet.getPesoPet() == null
                    ? NAO_INFORMADO
                    : pet.getPesoPet() + " kg";

            System.out.println((i + 1) + ". " + nomeCompleto
                    + " - " + Objects.toString(pet.getTipoPet(), NAO_INFORMADO)
                    + " - " + Objects.toString(pet.getSexo(), NAO_INFORMADO)
                    + " - " + enderecoFormatado
                    + " - " + idade
                    + " - " + peso
                    + " - " + Objects.toString(pet.getRacaPet(), NAO_INFORMADO));
        }
    }
    public void salvarPetArquivo(Pet pet) {
        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
        String nomeArquivo = dataHora + "-" + (pet.getNomePet() + pet.getSobrenomePet()).replace(" ", "").toUpperCase() + ".TXT";

        File pastaPets = new File(PASTA_PETS);
        pastaPets.mkdir();
        File arquvivo = new File(pastaPets, nomeArquivo);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(arquvivo))) {

            writer.write("1 - " + pet.getNomePet() + " " + pet.getSobrenomePet() + "\n");
            writer.write("2 - " + pet.getTipoPet() + "\n");
            writer.write("3 - " + pet.getSexo() + "\n");
            writer.write("4 - " + pet.getEndereco().getNomeCidade() + ", " + pet.getEndereco().getNomeRua() + ", " + pet.getEndereco().getNumeroCasa() + ", " + pet.getEndereco().getNomeBairro() + "\n");
            writer.write("5 - " + (pet.getIdadePet() == null ? NAO_INFORMADO : pet.getIdadePet() + " anos") + "\n");
            writer.write("6 - " + (pet.getPesoPet() == null ? NAO_INFORMADO : pet.getPesoPet() + " kg") + "\n");
            writer.write("7 - " + (pet.getRacaPet() == null ? NAO_INFORMADO : pet.getRacaPet()) + "\n");
            pet.setArquivoPet(arquvivo);

        } catch (IOException e){
            System.out.println("Erro ao salvar o pet no arquivo: " + e.getMessage());
        }
    }

    public void deletarPetSelecionado(Pet pet) {
        if (pet == null) {
            System.out.println("Nenhum pet foi selecionado.");
            MenuPet.showMenu();
            return;
        }
        while (true) {
            System.out.println("\n\nTem certeza que deseja DELETAR esse Pet da lista ?\n\n");
            System.out.println("[SIM] Confirmar\n[NÃO] Deletar ");
            String respostaDelete = input.nextLine().trim();

            if (respostaDelete.trim().equalsIgnoreCase("SIM")) {
                if (!Pet.listaPetCadastrados.contains(pet)){
                    System.out.println("O pet selecionado não está na lista.");
                    MenuPet.showMenu();
                    return;
                } if(pet.getArquivoPet() == null || !pet.getArquivoPet().exists()){
                    System.out.println("O arquivo do pet não foi encontrado.");
                    MenuPet.showMenu();
                    return;
                } if (Pet.listaPetCadastrados.contains(pet) && pet.getArquivoPet().exists()) {
                    Pet.listaPetCadastrados.remove(pet);
                    boolean deletado = pet.getArquivoPet().delete();
                    if (deletado) {
                        System.out.println("\n\nPet deletado com sucesso.\n\n");
                        MenuPet.showMenu();
                        return;
                    } else {
                        System.out.println("Não foi possível deletar o arquivo do pet.");
                        MenuPet.showMenu();
                        return;
                    }
                }
            } else {
                System.out.println("O pet selecionado não está na lista.");
            }
            if (respostaDelete.equalsIgnoreCase("NÃO") || respostaDelete.equalsIgnoreCase("NAO")){
                System.out.println("\n\nPet NÃO deletado.\n\n");
                MenuPet.showMenu();
                return;
            }
        }
    }
}
