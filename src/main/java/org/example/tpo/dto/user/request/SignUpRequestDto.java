package org.example.tpo.dto.user.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    private String username;
    private String nickname;
    private String password;
    private String passwordConfirm;
}