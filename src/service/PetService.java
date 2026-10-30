package service;

import model.Endereco;
import model.Pet;
import repository.PetRepository;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Scanner;

public class PetService {
    private static Scanner input = new Scanner(System.in);
    private static PetRepository petRepository = new PetRepository();
    private static PetAddressService petAddressService = new PetAddressService();
    private static PetService petService = new PetService();
    private static final String NAO_INFORMADO = "NAO_INFORMADO";


    public Double lerNumero(String resposta){
        String r = resposta.trim();
        if (r.isBlank()) {
            return null;
        }
        if (!r.matches("\\d+([.,]\\d+)?")) {
            throw new IllegalArgumentException("Digite apenas números.");
        }
        return Double.parseDouble(r.replace(',' , '.'));
    }

    private void addPets(Pet petCadastrado){
        Pet.listaPetCadastrados.add(petCadastrado);
    }
    public String normalizarTexto(String texto){
        String semAcentos = Normalizer.normalize(
                texto == null ? "" : texto, Normalizer.Form.NFD
        ).replaceAll("\\p{M}", "");
        return semAcentos.toLowerCase(Locale.ROOT);
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
                Pet.listaPetCadastrados.remove(pet);
                pet.getArquivoPet().delete();
                pet2.setNomePet(novoNome);
                petRepository.salvarPetArquivo(pet2);
                break;
            case "2":
                System.out.println("Informe o novo sobrenome: ");
                String novoSobrenome = input.nextLine();
                Pet pet3 = pet;
                Pet.listaPetCadastrados.remove(pet);
                pet.getArquivoPet().delete();
                pet3.setSobrenomePet(novoSobrenome);
                petRepository.salvarPetArquivo(pet3);
                break;
            case "3":
                Endereco novoEndereco = petAddressService.coletaEndereco(input);
                Pet pet4 = pet;
                Pet.listaPetCadastrados.remove(pet);
                pet.getArquivoPet().delete();
                pet4.setEndereco(novoEndereco);
                petRepository.salvarPetArquivo(pet4);
                break;
            case "4":
                System.out.println("Informe a nova idade: ");
                Double novaIdade = petService.lerNumero(input.nextLine());
                Pet pet5 = pet;
                Pet.listaPetCadastrados.remove(pet);
                pet.getArquivoPet().delete();
                pet5.setIdadePet(novaIdade);
                petRepository.salvarPetArquivo(pet5);
                break;
            case "5":
                System.out.println("Informe o novo peso: ");
                Double novoPeso = petService.lerNumero(input.nextLine());
                Pet pet6 = pet;
                Pet.listaPetCadastrados.remove(pet);
                pet.getArquivoPet().delete();
                pet6.setPesoPet(novoPeso);
                petRepository.salvarPetArquivo(pet6);
                break;
            case "6":
                System.out.println("Informe a nova raça: ");
                String novaRaca = input.nextLine();
                Pet pet7 = pet;
                Pet.listaPetCadastrados.remove(pet);
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
    public Double lerNumeroDoArquivo(String value, String suffix) {
        if (value.equals(NAO_INFORMADO)){
            return null;
        }
        return Double.parseDouble(value.replace(suffix, "").trim());
    }

}
