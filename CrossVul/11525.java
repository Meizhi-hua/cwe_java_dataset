package io.dropwizard.validation.selfvalidating;
import javax.annotation.Nullable;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ViolationCollector {
    private static final Pattern ESCAPE_PATTERN = Pattern.compile("\\$\\{");
    private boolean violationOccurred = false;
    private ConstraintValidatorContext context;
    public ViolationCollector(ConstraintValidatorContext context) {
        this.context = context;
    }
    public void addViolation(String message) {
        violationOccurred = true;
        String messageTemplate = escapeEl(message);
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation();
    }
    public void addViolation(String propertyName, String message) {
        violationOccurred = true;
        String messageTemplate = escapeEl(message);
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addPropertyNode(propertyName)
                .addConstraintViolation();
    }
    public void addViolation(String propertyName, Integer index, String message) {
        violationOccurred = true;
        String messageTemplate = escapeEl(message);
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addPropertyNode(propertyName)
                .addBeanNode().inIterable().atIndex(index)
                .addConstraintViolation();
    }
    public void addViolation(String propertyName, String key, String message) {
        violationOccurred = true;
        String messageTemplate = escapeEl(message);
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addPropertyNode(propertyName)
                .addBeanNode().inIterable().atKey(key)
                .addConstraintViolation();
    }
    @Nullable
    private String escapeEl(@Nullable String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        final Matcher m = ESCAPE_PATTERN.matcher(s);
        final StringBuffer sb = new StringBuffer(s.length() + 16);
        while (m.find()) {
            m.appendReplacement(sb, "\\\\\\${");
        }
        m.appendTail(sb);
        return sb.toString();
    }
    public ConstraintValidatorContext getContext() {
        return context;
    }
    public boolean hasViolationOccurred() {
        return violationOccurred;
    }
    public void setViolationOccurred(boolean violationOccurred) {
        this.violationOccurred = violationOccurred;
    }
}
