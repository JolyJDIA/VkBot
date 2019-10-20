package jolyjdia.bot.minecraft;

import api.Bot;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.jetbrains.annotations.NotNull;

public class ChatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public final void handlerAdded(@NotNull ChannelHandlerContext ctx) {
        System.out.println("коннект");
    }

    @Override
    public final void handlerRemoved(@NotNull ChannelHandlerContext ctx) {
        System.out.println("дисконнект");
    }
    @Override
    public final void channelRead(ChannelHandlerContext context, @NotNull Object o) {
        Bot.sendMessage(o.toString(), 310289867);
    }
}