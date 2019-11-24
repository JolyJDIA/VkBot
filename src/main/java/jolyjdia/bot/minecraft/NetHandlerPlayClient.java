package jolyjdia.bot.minecraft;

import api.utils.text.MessageReceiver;

public class NetHandlerPlayClient implements INetHandlerPlayClient {

    @Override
    public final void handleServer(SPacket packet) {
        MessageReceiver.sendMessage(String.valueOf(packet.getOnline()), 310289867);
    }
}