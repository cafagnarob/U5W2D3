package robertoCafagna.U5W2D3.payloads;

import jakarta.validation.constraints.*;


public record PostsDTO(
        @NotBlank(message = "inserire categoria")
        @Size(max = 30, message = "categoria troppo lunga")
        String categoria,
        @NotBlank(message = "inserire titolo")
        @Size(min = 2, max = 40, message = "il titolo deve essere tra 2 e 40 caratteri")
        String titolo,
        @NotBlank(message = "inserire un contenuto")
        @Size(min = 2, max = 1000, message = "inserire un contenuto tra i 2 e i 1000 caratteri")
        String contenuto,
        @NotNull(message = "inserire i minuti di lettura")
        @Min(value = 2, message = "minutaggio troppo corto, correggi i dati")
        @Max(value = 200, message = "minutaggio troppo lungo, correggi i dati")
        Integer minutiDiLettura,
        @NotNull(message = "inserisci un ID_autore valido")
        Long userId) {
}
