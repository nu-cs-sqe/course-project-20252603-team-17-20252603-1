package ui.swing;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UiMessagesTest {

	@Test
	void englishBundleLoadsDefaultKeys() {
		UiMessages en = new UiMessages(Locale.ENGLISH);
		assertEquals("Chess", en.get("app.title"));
		assertEquals("New game", en.get("button.new.game"));
	}

	@Test
	void frenchBundleUsesTranslatedAppTitle() {
		UiMessages fr = new UiMessages(Locale.FRENCH);
		assertEquals("Échecs", fr.get("app.title"));
		assertTrue(fr.get("button.new.game").length() > 0);
	}

	@Test
	void formatSubstitutesPlaceholders() {
		UiMessages en = new UiMessages(Locale.ENGLISH);
		String line = en.format("status.to.move", en.playerColorName("WHITE"));
		assertEquals("WHITE to move", line);
	}
}
