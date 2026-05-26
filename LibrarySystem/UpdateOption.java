
public enum UpdateOption {

    BookNAME(1),
    AUTHOR(2),
    CATEGORY(3),
    YEAR(4),
    ALL(5);

    private final int value;

    UpdateOption(int value) {

        this.value = value;
    }

    public int getValue() {

        return value;
    }

    public static UpdateOption fromInt(int value) {

        for (UpdateOption option : UpdateOption.values()) {

            if (option.getValue() == value) {

                return option;
            }
        }

        return null;
    }
}
