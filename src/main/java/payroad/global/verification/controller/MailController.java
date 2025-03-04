package payroad.global.verification.controller;


import static payroad.global.verification.dto.response.MailResponseDTO.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import payroad.domain.map.dto.MapResponse;
import payroad.global.response.ApiResponse;
import payroad.global.response.status.ErrorStatus;
import payroad.global.verification.dto.request.MailRequestDTO;
import payroad.global.verification.dto.response.MailResponseDTO;
import payroad.global.verification.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
@Slf4j
public class MailController {

    private final EmailService emailService;

    @Operation(summary = "인증번호 발송 api", description = "이메일에 인증번호를 발송하는 api입니다.<br>**반환 형식(리스트)**<br>")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", description = "성공",
        content = @Content(schema = @Schema(implementation = MailResponseDTO.MailSend.class))
    )
    @PostMapping("/send")
    public ApiResponse<MailSend> mailSend(@RequestBody MailRequestDTO.MailSend request) {
        MailSend mailSend = emailService.sendVerificationMail(request.getEmail());
        if (!mailSend.getStatus()) {
            return ApiResponse.onFailure(ErrorStatus.MAIL_NOT_SEND, mailSend);
        }
        return ApiResponse.onSuccess(mailSend);
    }

    @Operation(summary = "인증번호 인증 api", description = "입력받은 인증번호를 인증해주는 api입니다.<br>**반환 형식(리스트)**<br>")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", description = "성공",
        content = @Content(schema = @Schema(implementation = MailResponseDTO.MailVerify.class))
    )
    @PostMapping("/verify")
    public ApiResponse<MailVerify> mailVerify(@RequestBody @Valid MailRequestDTO.MailVerify request) {
        MailVerify mailVerify = emailService.verifyCode(request.getEmail(), request.getCode());
        return ApiResponse.onSuccess(mailVerify);
    }

//    @PostMapping("/send/temporary-password")
//    public ApiResponse<MailSend> sendTemporaryPassword(@RequestBody MailRequestDTO.MailSend request) {
//        MailSend mailSend = emailService.sendPassword(request.getEmail());
//        if (!mailSend.getStatus()) {
//            return ApiResponse.onFailure(ErrorStatus.MAIL_NOT_SEND, mailSend);
//        }
//        return ApiResponse.onSuccess(mailSend);
//    }
}
