package xyz.exception;

import java.io.Writer;

import org.slf4j.LoggerFactory;

import freemarker.core.Environment;
import freemarker.log.Logger;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;


public class FreemarkerExceptionHandler implements TemplateExceptionHandler {
    private static final Logger log = (Logger)LoggerFactory.getLogger(FreemarkerExceptionHandler.class);

    public void handleTemplateException(TemplateException te, Environment env,Writer out) throws TemplateException {
        log.warn("[Freemarker Error: " + te.getMessage() + "]");
        throw new ViewException("freemarker error",te);
    }
}
