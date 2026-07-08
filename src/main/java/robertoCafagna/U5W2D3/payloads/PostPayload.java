package robertoCafagna.U5W2D3.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class PostPayload {
    private String categoria;
    private String titolo;
    private String contenuto;
    private Integer minutiDiLettura;
}
