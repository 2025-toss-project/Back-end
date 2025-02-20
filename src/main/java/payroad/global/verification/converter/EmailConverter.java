package payroad.global.verification.converter;

import static payroad.global.verification.dto.response.MailResponseDTO.*;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class EmailConverter {

    public MailSend toMailSendResponse(String comment, Boolean status) {
        return MailSend.builder()
                .responseComment(comment)
                .status(status)
                .timeStamp(LocalDateTime.now())
                .build();
    }

    public MailVerify toMailVerifyResponse(Boolean check, String email) {
        return MailVerify.builder()
                .check(check)
                .email(email)
                .build();
    }
}
