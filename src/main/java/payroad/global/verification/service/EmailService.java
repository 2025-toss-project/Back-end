package payroad.global.verification.service;

import static payroad.global.verification.dto.response.MailResponseDTO.*;

import payroad.domain.member.Member;
import payroad.global.verification.converter.EmailConverter;
import payroad.global.verification.entity.EmailVerification;
import payroad.global.verification.repository.EmailVerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailConverter emailConverter;
    private final EmailVerificationRepository emailVerificationRepository;
    private final RedisTemplate<String, Object> redisTemplate;

//    private final MemberService memberService;

    private final String SUBJECT_VERIFY = "[PayRoad] 페이로드 인증 메일입니다.";

    private final String SUBJECT_PASSWORD = "[PayRoad] 임시 비밀번호 메일입니다.";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 12;


    @Transactional
    public MailSend sendVerificationMail(String email) {
        String code = makeVerificationCode();
        EmailVerification verification = new EmailVerification(email, code);

        if (emailVerificationRepository.existsById(getRedisKey(email))) { //redis에 email이 있으면 삭제
            emailVerificationRepository.deleteById(getRedisKey(email));
        }
        emailVerificationRepository.save(verification); //Redis에 인증 코드 저장

        String content = makeMessageForm(code, "인증코드");

        return sendMail(email, SUBJECT_VERIFY, content);
    }


    /**
     * @return 입력받은 인증번호가 유효한지 확인하는 메서드
     */
    public MailVerify verifyCode(String email, String code) {
        Optional<EmailVerification> verificationObject = emailVerificationRepository.findById(email);
        boolean check = false;
        if (verificationObject.isPresent()) {
            EmailVerification verification = verificationObject.get();
            if (verification.getVerificationCode().equals(code)) {
                emailVerificationRepository.deleteById(email);
                check = true;
            }
        }
        return emailConverter.toMailVerifyResponse(check, email);
    }

//    @Transactional
//    public MailSend sendPassword(String email) {
//        Member member = memberService.findMemberByEmail(email);
//
//        String password = makeTemporaryPassword();
//
//        String message = makeMessageForm(password, "임시 비밀번호");
//
//        memberService.createNewPassword(member, password);
//
//        return sendMail(email, SUBJECT_PASSWORD, message);
//    }

    public Long getTTL(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * @return 메일 인증 번호를 반환하는 메서드
     */
    private String makeVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    /**
     * @return 이메일에 해당하는 레디스 키를 반환 하는 메서드
     */
    private String getRedisKey(String email) {
        return "Email:" + email;
    }

    /**
     *
     * @param email
     * @param subject
     * @param content
     * @return
     */
    private MailSend sendMail(String email, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            messageHelper.setTo(email);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);

            mailSender.send(messageHelper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
            return emailConverter.toMailSendResponse("메일 전송에 실패했습니다.", false);
        }
        return emailConverter.toMailSendResponse("메일 전송에 성공했습니다.", true);
    }

    /**
     * @return 메일 양식을 반환하는 메서드
     */
    private String makeMessageForm(String value, String purpose) {
        StringBuffer message = new StringBuffer();
        message
            .append("<h1 style = 'text-align: center;'>[PayRoad]</h1>");
        message
            .append("<h3 style ='text-align: center;'>")
            .append(purpose)
            .append(" : <strong style='font-size: 32px; letter-spacing: 8px;'>")
            .append(value)
            .append("</strong></h3>");
        return message.toString();
    }

    /**
     * @return 임시 비밀번호를 반환하는 메서드
     */
    private String makeTemporaryPassword() {
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(new SecureRandom().nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

}
