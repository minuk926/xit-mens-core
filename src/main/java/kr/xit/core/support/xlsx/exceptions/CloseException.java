package kr.xit.core.support.xlsx.exceptions;

public class CloseException extends RuntimeException {

    public CloseException() {
        super();
    }

    public CloseException(String msg) {
        super(msg);
    }

    public CloseException(Exception e) {
        super(e);
    }

    public CloseException(String msg, Exception e) {
        super(msg, e);
    }
}
