package jolyjdia.bot.minecraft;

import api.Bot;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jetbrains.annotations.NotNull;

public class ChatServerHandler extends SimpleChannelInboundHandler<Object> {
    private final INetHandler packetListener = new NetHandlerPlayClient();
    @Override
    public final void handlerAdded(@NotNull ChannelHandlerContext ctx) {
        System.out.println("коннект");
    }

    @Override
    public final void handlerRemoved(@NotNull ChannelHandlerContext ctx) {
        System.out.println("дисконнект");
    }
    @Override
    public final void channelRead0(ChannelHandlerContext context, @NotNull Object packet) {
       // System.out.println(packet);
        //((Packet<INetHandler>) packet).processPacket(this.packetListener, context.channel());
        Bot.sendMessage(packet.toString(), 310289867);
    }
}