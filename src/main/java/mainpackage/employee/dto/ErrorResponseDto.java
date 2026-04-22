package mainpackage.employee.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        String detailMesage,
        LocalDateTime errorTime
) {
}
