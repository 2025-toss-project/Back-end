package payroad.global.security.service.redis;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payroad.global.response.exception.GeneralException;
import payroad.global.response.status.ErrorStatus;
import payroad.global.security.entity.RefreshToken;
import payroad.global.security.repository.RefreshTokenRepository;
import payroad.global.security.util.JwtUtil;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new GeneralException(ErrorStatus.BUDGET_NOT_FIND));
    }

    @Transactional
    public void saveRefreshToken(Long memberId, String refreshToken) {
        RefreshToken updatedRefreshToken = refreshTokenRepository.findByMemberId(memberId)
            .map(originalRefreshToken -> originalRefreshToken.update(refreshToken))
            .orElse(new RefreshToken(memberId, refreshToken));

        refreshTokenRepository.save(updatedRefreshToken);
    }

    @Transactional
    public void deleteRefreshToken(Long memberId) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(memberId);
        refreshToken.ifPresent(refreshTokenRepository::delete);
    }

    public boolean isValidRefreshToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token).isPresent();
    }

    public Map<String, String> refreshAccessToken(String token) {
        Long memberId = jwtUtil.getMemberId(token);
        String email = jwtUtil.getEmail(token);
//        RoleType roleType = RoleType.valueOf(jwtUtil.getRoleType(token));
        String newAccessToken = jwtUtil.createJwt(memberId, email, true);
        String refreshToken = jwtUtil.createJwt(memberId, email, false);

        RefreshToken updatedRefreshToken = refreshTokenRepository.findByMemberId(memberId)
            .map(originalRefreshToken -> originalRefreshToken.update(refreshToken))
            .orElse(new RefreshToken(memberId, refreshToken));
        refreshTokenRepository.save(updatedRefreshToken);

        Map<String, String> tokens = Map.of(
            "accessToken", newAccessToken,
            "refreshToken", refreshToken
        );

        return tokens;
    }
}
