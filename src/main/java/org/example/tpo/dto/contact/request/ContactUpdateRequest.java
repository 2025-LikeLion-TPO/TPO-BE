package org.example.tpo.dto.contact.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactUpdateRequest {

    @NotBlank(message = "contactName은 필수입니다")
    private String contactName;

    @NotNull(message = "temperature는 필수입니다")
    private Integer temperature;

    @NotBlank(message = "relationshipType은 필수입니다")
    private String relationshipType;

    @NotBlank(message = "contactMethod는 필수입니다")
    private String contactMethod;

    @NotBlank(message = "communicationFrequency는 필수입니다")
    private String communicationFrequency;

    @NotNull
    private Integer receiveCount = 0;

    private String relationshipMemo;
}
