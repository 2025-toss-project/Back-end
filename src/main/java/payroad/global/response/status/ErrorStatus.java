package payroad.global.response.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "로그인 인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    //메일관련 예외 코드
    MAIL_NOT_FIND(HttpStatus.BAD_REQUEST,"MAIL400","잘못된 메일입니다."),
    MAIL_NOT_SEND(HttpStatus.BAD_REQUEST,"MAIL401","메일 전송을 실패했습니다."),

    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}