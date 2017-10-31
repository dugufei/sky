package sky.compiler;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class SkyLogger {

	// Log
	public static final String	PROJECT				= "SkyCompiler";

	static final String			PREFIX_OF_LOGGER	= PROJECT + "::Compiler ";

	private Messager			msg;

	public SkyLogger(Messager messager) {
		msg = messager;
	}

	public void info(CharSequence info) {
		if (StringUtils.isNotEmpty(info)) {
			msg.printMessage(Diagnostic.Kind.NOTE, PREFIX_OF_LOGGER + info);
		}
	}

	public void error(CharSequence error) {
		if (StringUtils.isNotEmpty(error)) {
			msg.printMessage(Diagnostic.Kind.ERROR, PREFIX_OF_LOGGER + "An exception is encountered, [" + error + "]");
		}
	}

	public void error(Throwable error) {
		if (null != error) {
			msg.printMessage(Diagnostic.Kind.ERROR, PREFIX_OF_LOGGER + "An exception is encountered, [" + error.getMessage() + "]" + "\n" + formatStackTrace(error.getStackTrace()));
		}
	}

	public void warning(CharSequence warning) {
		if (StringUtils.isNotEmpty(warning)) {
			msg.printMessage(Diagnostic.Kind.WARNING, PREFIX_OF_LOGGER + warning);
		}
	}

	private String formatStackTrace(StackTraceElement[] stackTrace) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : stackTrace) {
			sb.append("    at ").append(element.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
