package mr_immortalz.com.modelqq.utils;

public class RequestResponse{

    int Status;
    boolean HasError;
    boolean HasResponse;
    String Message;
    String Method;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public boolean isHasError() {
        return HasError;
    }

    public void setHasError(boolean hasError) {
        HasError = hasError;
    }

    public boolean isHasResponse() {
        return HasResponse;
    }

    public void setHasResponse(boolean hasResponse) {
        HasResponse = hasResponse;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

}

