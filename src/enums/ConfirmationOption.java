package enums;

public enum ConfirmationOption {

    YES(1),
    NO(2);

    private final int value;

    ConfirmationOption(int value) {

        this.value = value;
    }

    public int getValue() {

        return value;
    }

    public static ConfirmationOption fromInt(int value) {

        for (ConfirmationOption option : ConfirmationOption.values()) {

            if (option.getValue() == value) {

                return option;
            }
        }

        return null;
    }
}
