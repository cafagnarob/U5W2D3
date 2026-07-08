package robertoCafagna.U5W2D3.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorPayload {
    private String message;
    private LocalDateTime timestamp;
}
