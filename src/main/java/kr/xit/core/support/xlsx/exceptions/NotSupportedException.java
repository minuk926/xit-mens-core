package kr.xit.core.support.xlsx.exceptions;

public class NotSupportedException extends RuntimeException {

    public NotSupportedException() {
        super();
    }

    public NotSupportedException(String msg) {
        super(msg);
    }

    public NotSupportedException(Exception e) {
        super(e);
    }

    public NotSupportedException(String msg, Exception e) {
        super(msg, e);
    }
}
