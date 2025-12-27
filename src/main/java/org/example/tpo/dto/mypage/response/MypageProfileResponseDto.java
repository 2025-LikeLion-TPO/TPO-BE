package org.example.tpo.dto.mypage.response;

import lombok.*;
import org.example.tpo.entity.Users;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MypageProfileResponseDto {
    private String profileImage;
    private String nickname;
    private Integer age;
    private String job;
    private String income;
    private Users.UserPreference preference;
}
