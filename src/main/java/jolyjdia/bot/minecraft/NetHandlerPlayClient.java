package jolyjdia.bot.minecraft;

import api.utils.chat.MessageChannel;

public class NetHandlerPlayClient implements INetHandlerPlayClient {

    @Override
    public final void handleServer(SPacket packet) {
        MessageChannel.sendMessage(String.valueOf(packet.getOnline()), 310289867);
    }
}