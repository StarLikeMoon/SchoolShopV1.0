package cn.chm.o2o.exceptions;

public class LocalAuthOperationException extends RuntimeException {

    private static final long serialVersionUID = -7313434209872172403L;

    public LocalAuthOperationException(String msg) {
        super(msg);
    }
}
