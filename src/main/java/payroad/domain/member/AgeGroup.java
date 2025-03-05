package payroad.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgeGroup {
    TWENTIES("20대", 20),
    THIRTIES("30대", 30),
    FORTIES("40대", 40),
    FIFTIES("50대", 50);

    private final String label;
    private final int age;

}
