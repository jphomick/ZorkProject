public class Action {
    String command, message, responseCode;
    int responseValue;

    Action(String command, String message) {
        this.command = command.toLowerCase();
        this.message = message;
        this.responseCode = "";
        this.responseValue = 0;
    }

    Action(String command, String message, String responseCode, int responseValue) {
        this.command = command.toLowerCase();
        this.message = message;
        this.responseCode = responseCode;
        this.responseValue = responseValue;
    }

    public aReturn use() {
        return new aReturn(message, responseCode, responseValue);
    }

    public String getCommand() {
        return command;
    }

    public void changeMessage(String newMessage) {
        message = newMessage;
    }
}