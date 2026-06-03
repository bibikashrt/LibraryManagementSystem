package enums;

public enum StudentUpdateOption {

    NAME(1),
    FACULTY(2),
    BATCH(3),
    ALL(4);

    private final int value;

    StudentUpdateOption(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static StudentUpdateOption fromInt(int value) {

        for (StudentUpdateOption option : values()) {

            if (option.getValue() == value) {

                return option;
            }
        }

        return null;
    }
}
