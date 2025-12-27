package org.example.tpo.dto.mypage.request;

import lombok.*;
import org.example.tpo.entity.Users;
import org.springframework.web.multipart.MultipartFile;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MypageProfileUpdateRequestDto {
    private MultipartFile profileImage;
    private String nickname;
    private Integer age;
    private String job;
    private String income;
    private Users.UserPreference preference;
}
