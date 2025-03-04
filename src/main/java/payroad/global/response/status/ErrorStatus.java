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


    // 예산금액 관리 예외 코드
    BUDGET_EMPTY(HttpStatus.BAD_REQUEST,"BUDGET400","예산금액 설정이 필요합니다."),
    BUDGET_NOT_FIND(HttpStatus.BAD_REQUEST,"BUDGET401","해당 예산 금액이 없습니다."),


    // 카테고리 관련 예외 코드
    CATEGORY_NOT_FIND(HttpStatus.BAD_REQUEST,"CATEGORY400","해당 카테고리를 찾을수없습니다."),


    // 지출 내용 관련 예외코드
    CONSUMPTION_NOT_FIND(HttpStatus.BAD_REQUEST,"CONSUMPTION400","해당 지출내역이 없습니다."),


    /*
     * member
     */
    MEMBER_NOT_FOUND_BY_MEMBER_ID(HttpStatus.BAD_REQUEST, "MEMBER4000",
        "해당 id를 가진 회원이 존재하지 않습니다."),
    MEMBER_NOT_FOUND_BY_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER4001",
        "해당 email을 가진 회원이 존재하지 않습니다."),
    MEMBER_DUPLICATE_BY_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER4002",
        "이미 가입된 email입니다. "),
    MEMBER_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER4003",
        "잘못된 비밀번호 입니다. "),    /*,

     /* login용 token 관련 예외 코드*/
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "JWT4000",
        "유효하지 않은 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "JWT4001",
        "해당 refresh token이 존재하지 않습니다."),
    TOKEN_IS_EXPIRED(HttpStatus.BAD_REQUEST, "JWT4002",
        "만료된 토큰입니다."),
    AUTHENTICATION_TYPE_IS_NOT_BEARER(HttpStatus.BAD_REQUEST, "JWT4003",
        "잘못된 토큰 타입입니다.")


    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}