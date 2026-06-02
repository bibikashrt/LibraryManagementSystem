package enums;

public enum MenuOption {
    ADD_BOOK(1),
    VIEW_BOOKS(2),
    UPDATE_BOOK(3),
    DELETE_BOOK(4),
    SEARCH_BOOK(5),
    ADD_STUDENT(6),
    VIEW_STUDENTS(7),
    SEARCH_STUDENT(8),
    UPDATE_STUDENT(9),
    DELETE_STUDENT(10),
    EXIT(11);

    private final int value;

    MenuOption(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MenuOption fromInt(int value) {
        for (MenuOption option : MenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return null;
    }
}
