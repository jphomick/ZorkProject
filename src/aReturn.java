public class aReturn {
    String message, code;
    int value;

    aReturn(String message) {
        this.message = message;
        this.code = "";
        this.value = 0;
    }

    aReturn(String message, String code, int value) {
        this.message = message;
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getValue() {
        return value;
    }
}