
public enum MenuOption {
    ADD_BOOK(1),
    VIEW_BOOKS(2),
    UPDATE_BOOK(3),
    DELETE_BOOK(4),
    SEARCH_BOOK(5),
    EXIT(6);

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
