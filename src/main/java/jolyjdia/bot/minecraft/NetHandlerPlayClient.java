package jolyjdia.bot.minecraft;

import api.Bot;

public class NetHandlerPlayClient implements INetHandlerPlayClient {

    @Override
    public final void handleServer(SPacket packet) {
        Bot.sendMessage(String.valueOf(packet.getOnline()), 310289867);
    }
}