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
        private String nomePet;
        private String sobrenomePet;
        private TipoPet tipoPet;
        private SexoPet sexo;
        private String racaPet;
        private Double pesoPet;
        private Double idadePet;
        private Endereco endereco = new Endereco();
        private File arquivoPet;
        public static List<Pet> listaPetCadastrados = new ArrayList<>();
        private static final String NAO_INFORMADO = "NAO_INFORMADO";


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

        public String getNomePet() {
            return nomePet;
        }

        public void setNomePet(String nomePet) {
            this.nomePet = nomePet;
        }

        public String getSobrenomePet() {
            return sobrenomePet;
        }

        public void setSobrenomePet(String sobrenomePet) {
            this.sobrenomePet = sobrenomePet;
        }

        public TipoPet getTipoPet() {
            return tipoPet;
        }

        public void setTipoPet(TipoPet tipoPet) {
            this.tipoPet = tipoPet;
        }

        public SexoPet getSexo() {
            return sexo;
        }

        public void setSexo(SexoPet sexo) {
            this.sexo = sexo;
        }

        public String getRacaPet() {
            return racaPet;
        }

        public void setRacaPet(String racaPet) {
            this.racaPet = racaPet;
        }

        public Double getPesoPet() {
            return pesoPet;
        }

        public void setPesoPet(Double pesoPet) {
            this.pesoPet = pesoPet;
        }

        public Double getIdadePet() {
            return idadePet;
        }

        public void setIdadePet(Double idadePet) {
            this.idadePet = idadePet;
        }

        public Endereco getEndereco() {
            return endereco;
        }

        public void setEndereco(Endereco endereco) {
            this.endereco = endereco;
        }

        public File getArquivoPet() {
            return arquivoPet;
        }

        public void setArquivoPet(File arquivoPet) {
            this.arquivoPet = arquivoPet;
        }

        public static void addPets(Pet petCadastrado){
            Pet.listaPetCadastrados.add(petCadastrado);

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