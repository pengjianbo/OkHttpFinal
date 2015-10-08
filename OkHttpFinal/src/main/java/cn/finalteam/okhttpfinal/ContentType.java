package cn.finalteam.okhttpfinal;

/**
 * Desction:文件内容类型
 * Author:pengjianbo
 * Date:15/9/21 上午10:53
 */
public enum ContentType {
    TEXT("text/plain; charset=UTF-8"),
    PNG("image/png; charset=UTF-8"),
    JPEG("image/jpeg; charset=UTF-8");

    private String contentType;

    private ContentType(String contentType){
        this.contentType = contentType;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
