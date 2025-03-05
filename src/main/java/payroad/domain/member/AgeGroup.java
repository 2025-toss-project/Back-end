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

    public static AgeGroup fromString(String label) {
        for (AgeGroup ageGroup : AgeGroup.values()) {
            if (ageGroup.getLabel().equalsIgnoreCase(label)) {
                return ageGroup;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + label);
    }

    public static AgeGroup fromInt(Integer age) {
        for (AgeGroup ageGroup : AgeGroup.values()) {
            if (ageGroup.getAge()==age) {
                return ageGroup;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + age);
    }

}
