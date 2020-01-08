package jolyjdia.bot.minecraft;

import io.netty.channel.Channel;
import jolyjdia.api.module.Module;

public class CraftServer implements Module {
    private Channel channel;
    private final int port = 2885;

    @Override
    public void onLoad() {
        /*Bot.getScheduler().runTaskAsynchronously(() -> {
            EventLoopGroup producer = new NioEventLoopGroup();
            EventLoopGroup consumer = new NioEventLoopGroup();
            try {
                ServerBootstrap bootstrap = new ServerBootstrap().group(producer, consumer)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new Initializer(new NetHandlerPlayServer(this), State.server));
                this.channel = bootstrap.bind(port).sync().channel().closeFuture().sync().channel();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                producer.shutdownGracefully();
                consumer.shutdownGracefully();
            }
        });*/
    }
}
