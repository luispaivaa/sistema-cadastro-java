package service;

import model.Endereco;

import java.util.Scanner;

public class PetAddressService {
    private static final String NAO_INFORMADO = "NAO_INFORMADO";

    public Endereco coletaEndereco(Scanner input) {
        Endereco novoEndereco = new Endereco();
        System.out.println("Nome da Cidade - ");
        novoEndereco.setNomeCidade(input.nextLine().trim());
        System.out.println("Nome do Bairro - ");
        novoEndereco.setNomeBairro(input.nextLine().trim());
        System.out.println("Nome da Rua - ");
        novoEndereco.setNomeRua(input.nextLine().trim());
        while (true) {
            System.out.println("Número da Casa - ");
            String numero = input.nextLine().trim();
            if (numero.isBlank()){
                novoEndereco.setNumeroCasa(NAO_INFORMADO);
                break;
            }
            if (numero.matches("[0-9]+")) {
                novoEndereco.setNumeroCasa(numero);
                break;
            }
            System.out.println("DIGITE APENAS NÚMEROS.\n");
        }
        return novoEndereco;
    }
    public Endereco lerEnderecoDoArquivo(String value){
        Endereco endereco = new Endereco();
        String[] partesDoEndereco = value.split(",");
        if (partesDoEndereco.length == 4){
            endereco.setNomeCidade(partesDoEndereco[0].trim());
            endereco.setNomeRua(partesDoEndereco[1].trim());
            endereco.setNumeroCasa(partesDoEndereco[2].trim());
            endereco.setNomeBairro(partesDoEndereco[3].trim());
        } else {
            endereco.setNomeCidade(value.trim());
        }
        return endereco;
    }
}
