package menu;

import model.Endereco;
import model.Pet;
import model.TipoPet;
import repository.PetRepository;
import service.PetAddressService;
import service.PetService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static model.Pet.listaPetCadastrados;

public class MenuPet {
    private static Scanner input = new Scanner(System.in);
    private static PetRepository petRepository = new PetRepository();
    private static PetService petService = new PetService();
    private static PetAddressService petAddressService = new PetAddressService();


    public static void showMenu() {
        System.out.println("\n\n--------BEM-VINDO AO SISTEMA DE CADASTRO---------\n");
        System.out.println("1 - Cadastrar um novo pet\n2 - Alterar os dados do pet cadastrado\n3 - Deletar um pet cadastrado\n4 - Listar todos os pets cadastrados\n5 - Listar pets por algum critério (idade, nome, raça)\n" +
                "6 - Sair");
        System.out.println("\nEscolha uma opção: ");
        String opcaoEscolhida = input.nextLine();
        switch (opcaoEscolhida.trim()) {
            case "1":
                petRepository.scanForms();
                break;
            case "2":
                menuAlterarPetCadastrado();
                break;
            case "3":
                menuDeletarPetCadastrado();
                break;
            case "4":
                if (listaPetCadastrados.isEmpty()) {
                    System.out.println("\n\nNenhum pet cadastrado.\n\n");
                    showMenu();
                } else {
                    System.out.println("\n\n--- PETS CADASTRADOS ---\n\n");
                    PetRepository.mostrarPetsPorLinha(listaPetCadastrados);
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
    private static void showForms(){
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
    private static void exibirMenuBuscaPets(){
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

    private static void menuAlterarPetCadastrado() {
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
                List<Pet> petsPorIdade = buscarPetsPorIdade(TipoPet.valueOf(tipoPetFormatado));
                if (petsPorIdade.isEmpty()) {
                    System.out.println("Nenhum pet encontrado com essa idade.");
                    exibirMenuBuscaPets();
                } else {
                    System.out.println("Qual pet deseja alterar? Informe o número correspondente: ");
                    int numeroPet = Integer.parseInt(input.nextLine());
                    if (numeroPet < 1 || numeroPet > petsPorIdade.size()) {
                        System.out.println("Número inválido.");
                        exibirMenuBuscaPets();
                    } else {
                        Pet petSelecionado = petsPorIdade.get(numeroPet - 1);
                        System.out.println("Pet selecionado: " + petSelecionado.getNomePet() + " " + petSelecionado.getSobrenomePet());
                        alterarPetCadastrado(petSelecionado);
                    }
                }
                break;
            case "2":
                List<Pet> petsPorNome = buscarPetsPorNome(TipoPet.valueOf(tipoPetFormatado));
                if (petsPorNome.isEmpty()) {
                    System.out.println("Nenhum pet encontrado com esse nome.");
                    exibirMenuBuscaPets();
                } else {
                    System.out.println("Qual pet deseja alterar? Informe o número correspondente: ");
                    int numeroPet = Integer.parseInt(input.nextLine());
                    if (numeroPet < 1 || numeroPet > petsPorNome.size()) {
                        System.out.println("Número inválido.");
                        exibirMenuBuscaPets();
                    } else {
                        Pet petSelecionado = petsPorNome.get(numeroPet - 1);
                        System.out.println("Pet selecionado: " + petSelecionado.getNomePet() + " " + petSelecionado.getSobrenomePet());
                        alterarPetCadastrado(petSelecionado);
                    }
                }
                break;
            case "3":
                List<Pet> petsPorRaca = buscarPetsPorRaca(TipoPet.valueOf(tipoPetFormatado));
                if (petsPorRaca.isEmpty()) {
                    System.out.println("Nenhum pet encontrado com essa raça.");
                    exibirMenuBuscaPets();
                } else {
                    System.out.println("Qual pet deseja alterar? Informe o número correspondente: ");
                    int numeroPet = Integer.parseInt(input.nextLine());
                    if (numeroPet < 1 || numeroPet > petsPorRaca.size()) {
                        System.out.println("Número inválido.");
                        exibirMenuBuscaPets();
                    } else {
                        Pet petSelecionado = petsPorRaca.get(numeroPet - 1);
                        System.out.println("Pet selecionado: " + petSelecionado.getNomePet() + " " + petSelecionado.getSobrenomePet());
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
    private static List<Pet> buscarPetsPorIdade(TipoPet tipoPet){
        System.out.println("INFORME A IDADE DE SEU PET: ");
        String resposta = input.nextLine();
        Double idade = petService.lerNumero(resposta);
        List<Pet> petsEncontrados = new ArrayList<>();
        for(Pet pet : listaPetCadastrados){
            if (pet.getTipoPet() == tipoPet && Objects.equals(pet.getIdadePet(), idade)){
                petsEncontrados.add(pet);
            }
        }
        if (petsEncontrados.isEmpty()){
            System.out.println("\n\nNenhum pet cadastrado com essa idade.\n\n");
            exibirMenuBuscaPets();
        } else {
            System.out.println("\n\n--- PETS ENCONTRADOS ---\n\n");
            PetRepository.mostrarPetsPorLinha(petsEncontrados);
        }
        return petsEncontrados;
    }
    private static List<Pet> buscarPetsPorNome(TipoPet tipoPet){
        System.out.println("INFORME O NOME DO SEU PET: ");
        String resposta = input.nextLine();
        String respostaFormatada = petService.normalizarTexto(resposta.trim());
        List<Pet> petsEncontradosNome = new ArrayList<>();

        for (Pet petCadastrado : listaPetCadastrados) {
            String nomeCompleto = petCadastrado.getNomePet() + " " + petCadastrado.getSobrenomePet();
            String nomeCompletoFormatado = petService.normalizarTexto(nomeCompleto.trim());
            if (petCadastrado.getTipoPet() == tipoPet && (nomeCompletoFormatado.contains(respostaFormatada))) {
                petsEncontradosNome.add(petCadastrado);
            }
        }
        if (petsEncontradosNome.isEmpty()){
            System.out.println("\n\nNenhum pet cadastrado com esse nome.\n\n");
            exibirMenuBuscaPets();
        } else {
            System.out.println("\n\n--- PETS ENCONTRADOS ---\n\n");
            PetRepository.mostrarPetsPorLinha(petsEncontradosNome);
        }
        return petsEncontradosNome;
    }
    private static List<Pet> buscarPetsPorRaca(TipoPet tipoPet){
        System.out.println("\n\nINFORME A RAÇA DO SEU PET: \n");
        String resposta = input.nextLine();
        String respostaFormatada = petService.normalizarTexto(resposta.trim());
        List<Pet> petsEncontradosRaca = new ArrayList<>();

        for (Pet petCadastrado : listaPetCadastrados) {
            String racaFormatada = petService.normalizarTexto(petCadastrado.getRacaPet().trim());
            if (petCadastrado.getTipoPet() == tipoPet && racaFormatada.contains(respostaFormatada)){
                petsEncontradosRaca.add(petCadastrado);
            }
        }
        if (petsEncontradosRaca.isEmpty()){
            System.out.println("Nenhum pet com essa raça foi cadastrado.");
            exibirMenuBuscaPets();
        } else {
            System.out.println("\n\n--- PETS ENCONTRADOS ---\n\n");
            PetRepository.mostrarPetsPorLinha(petsEncontradosRaca);
        }

        return petsEncontradosRaca;
    }
    private static void alterarPetCadastrado(Pet pet) {
        System.out.println("Qual campo deseja alterar?");
        System.out.println("1 - Nome\n2 - Sobrenome\n3 - Endereço\n4 - Idade\n5 - Peso\n6 - Raça");
        String opcaoEscolhida = input.nextLine();
        switch (opcaoEscolhida.trim()) {
            case "1":
                System.out.println("Informe o novo nome: ");
                String novoNome = input.nextLine();
                Pet pet2 = pet;
                listaPetCadastrados.remove(pet);
                pet.getArquivoPet().delete();
                pet2.setNomePet(novoNome);
                petRepository.salvarPetArquivo(pet2);
                break;
            case "2":
                System.out.println("Informe o novo sobrenome: ");
                String novoSobrenome = input.nextLine();
                Pet pet3 = pet;
                listaPetCadastrados.remove(pet);
                pet.getArquivoPet().delete();
                pet3.setSobrenomePet(novoSobrenome);
                petRepository.salvarPetArquivo(pet3);
                break;
            case "3":
                Endereco novoEndereco = petAddressService.coletaEndereco(input);
                Pet pet4 = pet;
                listaPetCadastrados.remove(pet);
                pet.getArquivoPet().delete();
                pet4.setEndereco(novoEndereco);
                petRepository.salvarPetArquivo(pet4);
                break;
            case "4":
                System.out.println("Informe a nova idade: ");
                Double novaIdade = petService.lerNumero(input.nextLine());
                Pet pet5 = pet;
                listaPetCadastrados.remove(pet);
                pet.getArquivoPet().delete();
                pet5.setIdadePet(novaIdade);
                petRepository.salvarPetArquivo(pet5);
                break;
            case "5":
                System.out.println("Informe o novo peso: ");
                Double novoPeso = petService.lerNumero(input.nextLine());
                Pet pet6 = pet;
                listaPetCadastrados.remove(pet);
                pet.getArquivoPet().delete();
                pet6.setPesoPet(novoPeso);
                petRepository.salvarPetArquivo(pet6);
                break;
            case "6":
                System.out.println("Informe a nova raça: ");
                String novaRaca = input.nextLine();
                Pet pet7 = pet;
                listaPetCadastrados.remove(pet);
                pet.getArquivoPet().delete();
                pet7.setRacaPet(novaRaca);
                petRepository.salvarPetArquivo(pet7);
                break;
            default:
                System.out.println("Opção inválida");
                alterarPetCadastrado(pet);
                break;
        }
    }

    private static void menuDeletarPetCadastrado(){
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
                List<Pet> petsPorIdade = buscarPetsPorIdade(TipoPet.valueOf(tipoPetFormatado));
                if (petsPorIdade.isEmpty()) {
                    System.out.println("Nenhum pet encontrado com essa idade.");
                    exibirMenuBuscaPets();
                } else {
                    System.out.println("Qual pet deseja deletar? Informe o número correspondente: ");
                    int numeroPet = Integer.parseInt(input.nextLine());
                    if (numeroPet < 1 || numeroPet > petsPorIdade.size()) {
                        System.out.println("Número inválido.");
                        exibirMenuBuscaPets();
                    } else {
                        Pet petSelecionado = petsPorIdade.get(numeroPet - 1);
                        System.out.println("Pet selecionado: " + petSelecionado.getNomePet() + " " + petSelecionado.getSobrenomePet());
                        petRepository.deletarPetSelecionado(petSelecionado);
                    }
                }
                break;
            case "2":
                List<Pet> petsPorNome = buscarPetsPorNome(TipoPet.valueOf(tipoPetFormatado));
                if (petsPorNome.isEmpty()) {
                    System.out.println("Nenhum pe4t encontrado com esse nome.");
                    exibirMenuBuscaPets();
                } else {
                    System.out.println("Qual pet deseja deletar? Informe o número correspondente: ");
                    int numeroPet = Integer.parseInt(input.nextLine());
                    if (numeroPet < 1 || numeroPet > petsPorNome.size()) {
                        System.out.println("Número inválido.");
                        exibirMenuBuscaPets();
                    } else {
                        Pet petSelecionado = petsPorNome.get(numeroPet - 1);
                        System.out.println("Pet selecionado: " + petSelecionado.getNomePet() + " " + petSelecionado.getSobrenomePet());
                        petRepository.deletarPetSelecionado(petSelecionado);
                    }
                }
                break;
            case "3":
                List<Pet> petsPorRaca = buscarPetsPorRaca(TipoPet.valueOf(tipoPetFormatado));
                if (petsPorRaca.isEmpty()) {
                    System.out.println("Nenhum pet encontrado com essa raça.");
                    exibirMenuBuscaPets();
                } else {
                    System.out.println("Qual pet deseja deletar? Informe o número correspondente: ");
                    int numeroPet = Integer.parseInt(input.nextLine());
                    if (numeroPet < 1 || numeroPet > petsPorRaca.size()) {
                        System.out.println("Número inválido.");
                        exibirMenuBuscaPets();
                    } else {
                        Pet petSelecionado = petsPorRaca.get(numeroPet - 1);
                        System.out.println("Pet selecionado: " + petSelecionado.getNomePet() + " " + petSelecionado.getSobrenomePet());
                        petRepository.deletarPetSelecionado(petSelecionado);
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
                petRepository.scanForms();
                return;
            }
            System.out.println("DIGITE UMA RESPOSTA VÁLIDA.");
        }
    }
}