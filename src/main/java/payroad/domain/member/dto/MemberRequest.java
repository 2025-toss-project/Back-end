package payroad.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class MemberRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDTO{
        @NotNull
        @Email
        private String email;

        @NotNull
        private String password;

        @NotNull
        private String nickName;

        @NotNull
        private String gender;

        @NotNull
        private String type;

        @NotNull
        private Double lat;

        @NotNull
        private Double lng;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordDTO{
        @NotNull
        private String oldPassword;

        @NotNull
        private String newPassword;
    }
}
