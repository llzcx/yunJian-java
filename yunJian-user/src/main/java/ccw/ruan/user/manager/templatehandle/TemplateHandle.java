package ccw.ruan.user.manager.templatehandle;

/**
 * @author 陈翔
 */
public interface TemplateHandle {
    /**
     * 处理字符串
     * @param text
     * @return
     */
    String handle(String text);

    /**
     * 设置下一个
     * @param handler
     */
    TemplateHandle setNext(TemplateHandle handler);
}



