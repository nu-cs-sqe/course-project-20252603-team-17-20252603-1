package ui.swing;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Loads GUI strings from {@code messages*.properties} via {@link ResourceBundle}.
 * Locale: {@code Locale.getDefault()} unless overridden with {@value #LOCALE_PROPERTY}
 * (e.g. {@code -Dchess.locale=fr}).
 */
public final class UiMessages {

	/** JVM system property for BCP 47 language tag (e.g. {@code en}, {@code fr}). */
	public static final String LOCALE_PROPERTY = "chess.locale";

	private static final String BUNDLE_BASE = "messages";

	private final ResourceBundle bundle;

	public UiMessages() {
		this(resolveLocale());
	}

	public UiMessages(Locale locale) {
		bundle = ResourceBundle.getBundle(BUNDLE_BASE, locale);
	}

	/**
	 * Resolves the GUI locale: use the {@value #LOCALE_PROPERTY} system property when set,
	 * otherwise {@link Locale#getDefault()}.
	 */
	public static Locale resolveLocale() {
		String tag = System.getProperty(LOCALE_PROPERTY, "").trim();
		if (!tag.isEmpty()) {
			return Locale.forLanguageTag(tag);
		}
		return Locale.getDefault();
	}

	public String get(String key) {
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return "!" + key + "!";
		}
	}

	public String format(String key, Object... args) {
		return MessageFormat.format(get(key), args);
	}

	/** Display name for a model color token ({@code WHITE} / {@code BLACK}). */
	public String playerColorName(String modelColor) {
		if ("WHITE".equals(modelColor)) {
			return get("color.WHITE");
		}
		if ("BLACK".equals(modelColor)) {
			return get("color.BLACK");
		}
		return modelColor;
	}

	public String drawReasonLabel(String code) {
		if (code == null) {
			return get("draw.reason.unknown");
		}
		String key = "draw.reason." + code;
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return code.toLowerCase(Locale.ROOT).replace('_', ' ');
		}
	}
}
