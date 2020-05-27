package cn.chm.o2o.dto;

/**
 * 封装JSON对象，返回结果给前端
 * @param <E>
 */
public class Result<E> {

    // 操作是否成功
    private boolean success;
    // 返回的数据
    private E data;
    // 错误信息
    private String errMsg;
    // 错误代码
    private int errorCode;

    public Result() {
    }

    // 操作成功时
    public Result(boolean success, E data) {
        this.success = success;
        this.data = data;
    }

    //操作失败时
    public Result(boolean success, String errMsg, int errorCode) {
        this.success = success;
        this.errMsg = errMsg;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
