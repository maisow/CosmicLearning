package learn.cosmiclearning.domain;

import java.util.ArrayList;
import java.util.List;

public class Result<T>  {


    private T payload;

    private ResultType resultType = ResultType.SUCCESS;

    private final List<String> messages = new ArrayList<>();

    public List<String> getErrorMessages() {
        return new ArrayList<>(messages);
    }

    public void addErrorMessage(ResultType resultType, String message) {
        this.resultType = resultType;
        messages.add(message);
    }

    public void addErrorMessage(String message) {
        addErrorMessage(ResultType.INVALID, message);
    }

    public void addErrorMessage(String message, Object... args) {
        addErrorMessage(String.format(message, args));
    }

    public void addErrorMessage(ResultType resultType, String message, Object... args) {
        addErrorMessage(resultType, String.format(message, args));
    }

    public boolean isSuccess() {
        return resultType == ResultType.SUCCESS;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public ResultType getResultType() {
        return resultType;
    }
}
