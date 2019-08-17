package jolyjdia.bot.translator.language;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public enum Language {
  ALBANIAN("sq"),
  ARMENIAN("hy"),
  AZERBAIJANI("az"),
  BELARUSIAN("be"),
  BULGARIAN("bg"),
  CATALAN("ca"),
  CROATIAN("hr"),
  CZECH("cs"),
  DANISH("da"),
  DUTCH("nl"),
  ENGLISH("en"),
  ESTONIAN("et"),
  FINNISH("fi"),
  FRENCH("fr"),
  GERMAN("de"),
  GEORGIAN("ka"),
  GREEK("el"),
  HUNGARIAN("hu"),
  ITALIAN("it"),
  LATVIAN("lv"),
  LITHUANIAN("lt"),
  MACEDONIAN("mk"),
  NORWEGIAN("no"),
  POLISH("pl"),
  PORTUGUESE("pt"),
  ROMANIAN("ro"),
  RUSSIAN("ru"),
  SERBIAN("sr"),
  SLOVAK("sk"),
  SLOVENIAN("sl"),
  SPANISH("es"),
  SWEDISH("sv"),
  TURKISH("tr"),
  UKRAINIAN("uk");

  /**
   * String representation of this language.
   */
  private final String language;

  @Contract(pure = true)
  Language(String pLanguage) {
    language = pLanguage;
  }

  @Nullable
  public static Language fromString(String pLanguage) {
    for (Language l : values()) {
      if (l.toString().equals(pLanguage)) {
        return l;
      }
    }
    return null;
  }

  @Contract(pure = true)
  @Override
  public String toString() {
    return language;
  }

}
