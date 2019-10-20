package jolyjdia.bot.minecraft;

import api.module.Module;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.jetbrains.annotations.NotNull;

public class CraftClient implements Module {

    @Override
    public final void onLoad() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap  = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SocketChannelChannelInitializer());
            bootstrap.connect("192.168.0.104", 2885).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class SocketChannelChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected final void initChannel(@NotNull SocketChannel socketChannel) {
            ChannelPipeline pipeline = socketChannel.pipeline();

            pipeline.addLast("decoder", new StringDecoder());
            pipeline.addLast("encoder", new StringEncoder());

            pipeline.addLast("handler", new ChatServerHandler());
        }
    }
}
