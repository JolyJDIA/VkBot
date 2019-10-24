package jolyjdia.bot.minecraft;

import io.netty.channel.Channel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SPacket implements Packet<INetHandlerPlayClient> {
    private String server;
    private int online;

    @Override
    public void readPacketData(PacketBuffer buf) {
        this.server = buf.readString(256);
        this.online = buf.readInt();
    }

    @Override
    public void writePacketData(@NotNull PacketBuffer buf) {
        buf.writeVarInt(0);
        buf.writeString(this.server);
        buf.writeInt(this.online);
    }

    @Override
    public void processPacket(@NotNull INetHandlerPlayClient handler, Channel channel) {
        handler.handleServer(this);
    }

    @Contract(pure = true)
    public final String getServer() {
        return this.server;
    }

    @Contract(pure = true)
    public final int getOnline() {
        return this.online;
    }
}