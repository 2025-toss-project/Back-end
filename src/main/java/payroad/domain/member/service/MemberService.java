package payroad.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import payroad.domain.member.dto.MemberConverter;
import payroad.domain.member.dto.MemberRequest;
import payroad.domain.member.dto.MemberResponse;
import payroad.domain.member.repository.MemberRepository;
import payroad.global.response.exception.GeneralException;
import payroad.global.response.status.ErrorStatus;
import payroad.domain.member.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;


    private final MemberConverter memberConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Value("${jwt.access-token-validity-in-seconds}")
    private Long ACCESS_TOKEN_VALIDITY_IN_SECONDS;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long REFRESH_TOKEN_VALIDITY_IN_SECONDS;


    @Transactional
    public MemberResponse.JoinResponse join(MemberRequest.JoinDTO request) {
        Optional.of(request.getEmail()) //해당 매일이 존재하는 경우 멤버 중복 예외 처리
            .filter(email -> !memberRepository.existsByEmail(email))
            .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_DUPLICATE_BY_EMAIL));

        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(request.getHome().getLat(),request.getHome().getLng()));
        point.setSRID(4326); // SRID 설정

        Member newMember = memberConverter.toEntity(request,point);
        newMember.setPassword(bCryptPasswordEncoder.encode(request.getPassword())); // 비밀번호는 인코딩해서 넣음
        Member member = memberRepository.save(newMember);

        return memberConverter.toJoinResponse(member);
    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
            () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND_BY_MEMBER_ID)
        );
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
            () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND_BY_EMAIL)
        );
    }

    @Transactional
    public MemberResponse.JoinResponse changePassword(Member member, MemberRequest.ChangePasswordDTO request) {
        if (!bCryptPasswordEncoder.matches(request.getOldPassword(), member.getPassword())) {
            throw new GeneralException(ErrorStatus.MEMBER_INVALID_PASSWORD);
        }

        return memberConverter.toJoinResponse(
            createNewPassword(member, request.getNewPassword())
        );
    }

    @Transactional
    public Member createNewPassword(Member member, String newPassword) {
        member.setPassword(bCryptPasswordEncoder.encode(newPassword));
        return memberRepository.save(member);
    }

}
