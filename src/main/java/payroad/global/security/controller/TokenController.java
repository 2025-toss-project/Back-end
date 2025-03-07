package payroad.global.security.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import payroad.global.response.ApiResponse;
import payroad.global.response.status.ErrorStatus;
import payroad.global.security.dto.RefreshDTO;
import payroad.global.security.service.redis.RefreshTokenService;
import payroad.global.security.util.JwtUtil;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public ApiResponse<Map<String, String>> refreshAccessToken(
        @RequestBody RefreshDTO refreshDTO) {
        if (!jwtUtil.validateToken(refreshDTO.getRefreshToken())) {
            return ApiResponse.onFailure(
                ErrorStatus.INVALID_TOKEN, Map.of("error", "RefreshToken이 유효하지 않습니다."));
        }

        if (!refreshTokenService.isValidRefreshToken(refreshDTO.getRefreshToken())) {
            return ApiResponse.onFailure(ErrorStatus.INVALID_TOKEN,
                Map.of("error", "RefreshToken이 유효하지 않습니다."));
        }

        Map<String, String> result = refreshTokenService.refreshAccessToken(refreshDTO.getRefreshToken());

        return ApiResponse.onSuccess(result);
    }
}