package org.example.tpo.dto.user.response;

import lombok.*;
import org.example.tpo.entity.Users;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String username;
    private String nickname;
    private Integer age;
    private String job;
    private Long income;
    private Users.UserPreference preference;
}
