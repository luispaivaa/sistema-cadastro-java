import menu.MenuPet;
import repository.PetRepository;

public class Main {
    public static void main(String[] args) {
        PetRepository.carregarPetsDoArquivo();
        MenuPet.showMenu();
      }
}
