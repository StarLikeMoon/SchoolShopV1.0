package cn.chm.o2o.exceptions;

public class ProductCategoryException extends RuntimeException {

    private static final long serialVersionUID = 6686595442886146699L;

    public ProductCategoryException(String msg) {
        super(msg);
    }
}
