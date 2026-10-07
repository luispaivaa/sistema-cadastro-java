package model;

public class Endereco {
     String nomeRua;
     String nomeBairro;
     String nomeCidade;
     String numeroCasa;

     public Endereco(String nomeRua, String nomeBairro, String nomeCidade, String numeroCasa) {
          this.nomeRua = nomeRua;
          this.nomeBairro = nomeBairro;
          this.nomeCidade = nomeCidade;
          this.numeroCasa = numeroCasa;
     }

     public Endereco() { 

     }

     @Override
     public String toString() {
          return "Endereco{" +
                  "nomeRua='" + nomeRua + '\'' +
                  ", nomeBairro='" + nomeBairro + '\'' +
                  ", nomeCidade='" + nomeCidade + '\'' +
                  ", numeroCasa=" + numeroCasa +
                  '}';
     }

     public String getNomeRua() {
          return nomeRua;
     }

     public void setNomeRua(String nomeRua) {
          this.nomeRua = nomeRua;
     }

     public String getNomeBairro() {
          return nomeBairro;
     }

     public void setNomeBairro(String nomeBairro) {
          this.nomeBairro = nomeBairro;
     }

     public String getNomeCidade() {
          return nomeCidade;
     }

     public void setNomeCidade(String nomeCidade) {
          this.nomeCidade = nomeCidade;
     }

     public String getNumeroCasa() {
          return numeroCasa;
     }

     public void setNumeroCasa(String numeroCasa) {
          this.numeroCasa = numeroCasa;
     }
}
