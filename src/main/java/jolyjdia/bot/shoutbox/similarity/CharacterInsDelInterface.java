package jolyjdia.bot.shoutbox.similarity;

public interface CharacterInsDelInterface {
    double deletionCost(char c);

    double insertionCost(char c);
}
