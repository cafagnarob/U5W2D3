package robertoCafagna.U5W2D3.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;


public record UsersDTO(
        @NotBlank(message = "inserisci un nome valido")
        @Size(min = 2, max = 40, message = "Il nome deve avere un numero di caratteri compreso tra 2 e 40")
        String nome,
        @NotBlank(message = "inserisci un cognome valido")
        @Size(min = 2, max = 40, message = "Il cognome deve avere un numero di caratteri compreso tra 2 e 40")
        String cognome,
        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "L'email deve essere nel formato corretto")
        String email,
        @Past(message = "La data di nascita deve essere nel passato")
        LocalDate dataDiNascita) {
}
