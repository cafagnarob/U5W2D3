package robertoCafagna.U5W2D3.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@ToString
public class UserPayload {
    private String nome;
    private String cognome;
    private String email;
    private LocalDate dataDiNascita;
}
