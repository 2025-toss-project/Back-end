package payroad.domain.member.dto;

import java.util.List;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import payroad.domain.member.Gender;
import payroad.domain.member.Member;
import payroad.domain.member.Type;

@Component
public class MemberConverter {

    public Member toEntity(MemberRequest.JoinDTO request, Point point) {
        return Member.builder()
            .email(request.getEmail())
            .nickname(request.getNickName())
            .gender(Gender.valueOf(request.getGender()))
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

//    public MemberResponse.MemberInfo toMemberInfo(Member member, List<MemberMajor> memberMajorList) {
//        List<MemberResponse.MajorInfo> majorList = memberMajorList.stream().map(
//            this::toMajorInfo
//        ).toList();
//
//        return MemberResponse.MemberInfo.builder()
//            .name(member.getName())
//            .email(member.getEmail())
//            .joinYear(member.getJoinYear() % 100)
//            .majorList(majorList)
//            .build();
//    }

//    public MemberResponse.MajorInfo toMajorInfo(MemberMajor memberMajor) {
//        return MemberResponse.MajorInfo.builder()
//            .majorType(memberMajor.getMajorType())
//            .departmentInfoDTO(DepartmentConverter.toDto(memberMajor.getDepartment()))
//            .build();
//    }
}
