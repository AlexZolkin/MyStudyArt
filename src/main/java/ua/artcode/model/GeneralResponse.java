package ua.artcode.model;

/**
 * Created by Алексей on 17.04.2017.
 */
public class GeneralResponse {
    private String message;

    public GeneralResponse() {
    }

    public GeneralResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GeneralResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
