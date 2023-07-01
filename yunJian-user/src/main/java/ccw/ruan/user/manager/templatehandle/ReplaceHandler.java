package ccw.ruan.user.manager.templatehandle;

/**
 * @author 陈翔
 */
public class ReplaceHandler implements TemplateHandle {
    private String target;
    private String replacement;
    private TemplateHandle nextHandler;

    public ReplaceHandler(String target, String replacement) {
        this.target = target;
        this.replacement = replacement;
    }

    @Override
    public TemplateHandle setNext(TemplateHandle handler) {
        this.nextHandler = handler;
        return this;
    }

    @Override
    public String handle(String text) {
        String name = "${" + target + "}";
        if (text.contains(name)) {
            text = text.replace(name, replacement);
        }
        if (nextHandler != null) {
            return nextHandler.handle(text);
        }
        return text;
    }
}