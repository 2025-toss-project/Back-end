package payroad.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import payroad.domain.member.Member;
import payroad.domain.member.dto.MemberConverter;
import payroad.domain.member.dto.MemberRequest;
import payroad.domain.member.dto.MemberResponse;
import payroad.domain.member.dto.MemberResponse.JoinResponse;
import payroad.domain.member.service.MemberService;
import payroad.global.response.ApiResponse;
import payroad.global.security.annotation.LoginMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ApiResponse<MemberResponse.JoinResponse> join(@RequestBody @Valid MemberRequest.JoinDTO request) {
        MemberResponse.JoinResponse joinResponse = memberService.join(request);

        return ApiResponse.onSuccess(joinResponse);
    }

    @PostMapping("/change-password")
    @Operation(summary = "회원 비밀번호 변경 api",
        description =
            "old password : 이전 비밀번호" + "<br>"
                + "new password : 새 비밀 번호")
    public ApiResponse<JoinResponse> changePassword(
        @LoginMember Member member,
        @RequestBody @Valid MemberRequest.ChangePasswordDTO request) {

        return ApiResponse.onSuccess(memberService.changePassword(member, request));
    }

    @GetMapping("/info")
    @Operation(summary = "회원 정보 조회 api", description = "현재 로그인한 회원의 정보를 반환하는 api입니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", description = "성공",
        content = @Content(schema = @Schema(implementation = MemberResponse.MemberInfo.class))
    )
    public ApiResponse<MemberResponse.MemberInfo> getMemberInfo(@LoginMember Member member) {
        MemberResponse.MemberInfo memberInfo = memberService.getMemberInfo(member);

        return ApiResponse.onSuccess(memberInfo);
    }

}
