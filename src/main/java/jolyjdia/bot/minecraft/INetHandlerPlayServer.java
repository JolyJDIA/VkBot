package jolyjdia.bot.minecraft;

public interface INetHandlerPlayServer extends INetHandler {
    void processServerInfo(CPacket packet);

}