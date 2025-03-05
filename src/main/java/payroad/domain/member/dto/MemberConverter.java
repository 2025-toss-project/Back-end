package payroad.domain.member.dto;

import java.util.List;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import payroad.domain.member.AgeGroup;
import payroad.domain.member.Member;
import payroad.domain.member.Type;
import payroad.domain.member.dto.MemberResponse.Home;
import payroad.domain.member.dto.MemberResponse.MemberInfo;

@Component
public class MemberConverter {

    public Member toEntity(MemberRequest.JoinDTO request, Point point) {
        return Member.builder()
            .email(request.getEmail())
            .nickname(request.getNickName())
            .ageGroup(AgeGroup.fromString(request.getAgeGroup()))
            .myLocation(point)
            .type(Type.valueOf(request.getType())) //todo : 내 집 위치도 찍어줘야함
            .build();
    }

    public MemberResponse.JoinResponse toJoinResponse(Member member) {
        return MemberResponse.JoinResponse.builder()
            .email(member.getEmail())
            .join_date(member.getCreatedAt())
            .build();
    }

    public MemberResponse.MemberInfo toMemberInfo(Member member) {

        return MemberInfo.builder()
            .nickname(member.getNickname())
            .email(member.getEmail())
            .ageGroup(member.getAgeGroup().getLabel())
            .home(Home.builder()
                .lan(member.getMyLocation().getX())
                .lat(member.getMyLocation().getY())
                .build())
            .type(member.getType().toString())
            .build();
    }

//    public MemberResponse.MajorInfo toMajorInfo(MemberMajor memberMajor) {
//        return MemberResponse.MajorInfo.builder()
//            .majorType(memberMajor.getMajorType())
//            .departmentInfoDTO(DepartmentConverter.toDto(memberMajor.getDepartment()))
//            .build();
//    }
}
