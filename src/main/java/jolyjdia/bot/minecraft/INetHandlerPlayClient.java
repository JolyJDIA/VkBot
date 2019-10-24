package jolyjdia.bot.minecraft;

@FunctionalInterface
public interface INetHandlerPlayClient extends INetHandler {
    void handleServer(SPacket packet);

}