package cn.chm.o2o.exceptions;

public class ShopOperationException extends RuntimeException {
    private static final long serialVersionUID = 6503928828824659681L;

    public ShopOperationException(String msg) {
        super(msg);
    }
}
