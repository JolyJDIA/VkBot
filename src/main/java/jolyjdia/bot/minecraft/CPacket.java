package jolyjdia.bot.minecraft;

import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;


public class CPacket implements Packet<INetHandlerPlayServer> {
    private String server;
    private int online;

    @Override
    public final void readPacketData(@NotNull PacketBuffer buf) {
        this.server = buf.readString(256);
        this.online = buf.readInt();
    }

    @Override
    public final void writePacketData(@NotNull PacketBuffer buf) {
        buf.writeVarInt(1);
        buf.writeString(this.server);
        buf.writeInt(this.online);
    }
    @Override
    public void processPacket(INetHandlerPlayServer handler, Channel channel) {
        handler.processServerInfo(this);
    }

    public String getServer() {
        return this.server;
    }

    public int getOnline() {
        return this.online;
    }
}