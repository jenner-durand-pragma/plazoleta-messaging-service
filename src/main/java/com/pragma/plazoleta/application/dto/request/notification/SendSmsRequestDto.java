package com.pragma.plazoleta.application.dto.request.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendSmsRequestDto {

    @NotBlank(message = "Destination phone is required")
    @Pattern(
            regexp = "^\\+?\\d{1,13}$",
            message = "Phone must be valid format"
    )
    @Schema(
            description = "Destination phone",
            example = "+51938475869"
    )
    private String to;

    @NotBlank(message = "Message body is required")
    @Size(max = 1600, message = "Message body cannot exceed 1600 characters")
    @Schema(
            description = "SMS body",
            example = "Your order #12 is ready. PIN: 482910"
    )
    private String message;
}
