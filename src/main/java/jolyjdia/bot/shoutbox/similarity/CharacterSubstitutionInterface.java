package jolyjdia.bot.shoutbox.similarity;

@FunctionalInterface
public interface CharacterSubstitutionInterface {
    double cost(char c1, char c2);
}
