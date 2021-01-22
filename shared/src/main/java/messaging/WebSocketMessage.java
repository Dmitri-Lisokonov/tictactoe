package messaging;

public class WebSocketMessage {
    private Operation operation;
    private int lobbyId;
    private String message;
    private String responseMessage;

    /**
     * Constructors.
     */
    public WebSocketMessage(){};
    public WebSocketMessage(Operation operation, String message, String responseMessage) {
        this.operation = operation;
        this.message = message;
        this.responseMessage = responseMessage;
    }

    /**
     * Getters and setters.
     */

    public int getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
