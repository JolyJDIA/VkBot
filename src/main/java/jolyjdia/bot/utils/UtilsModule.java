package jolyjdia.bot.utils;

import api.event.EventLabel;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.event.messages.SendCommandEvent;
import api.event.post.NewPostWallEvent;
import api.module.Module;
import api.utils.VkUtils;
import api.utils.chat.MessageChannel;
import com.google.common.collect.Maps;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import com.vk.api.sdk.objects.wall.Wallpost;
import jolyjdia.bot.Bot;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UtilsModule implements Module, Listener {
    private final Map<Integer, Long> cooldown = Maps.newHashMap();
    private PhotoTool tool;// = new PhotoTool();

    @Override
    public final void onLoad() {
        Bot.getBotManager().registerCommand(new PasswordCommand());
        Bot.getBotManager().registerCommand(new RaidCommand());
        Bot.getBotManager().registerCommand(new RollCommand());
        Bot.getBotManager().registerCommand(new FlipCommand());
        Bot.getBotManager().registerEvent(this);

        Bot.getScheduler().scheduleSyncRepeatingTask(() ->
                        cooldown.entrySet().removeIf(e -> (e.getValue() - System.currentTimeMillis()) < 1L),
                0, 50);
    }

    @EventLabel(ignoreCancelled = true)
    public final void onCommand(@NotNull SendCommandEvent e) {
        int userId = e.getUser().getUserId();
        if (cooldown.containsKey(userId)) {
            e.getUser().getChat().sendMessage("Подождите 1 секунду, перед тем, как использовать команду снова");
            e.setCancelled(true);
        } else {
            cooldown.put(userId, System.currentTimeMillis());
        }
    }
    @EventLabel
    public static void onPost(@NotNull NewPostWallEvent e) {
        Wallpost wallpost = e.getWallpost();
        Bot.getUserBackend().getChats().forEach(id -> MessageChannel.sendAttachments(VkUtils.attachment(wallpost), id));
    }
    @EventLabel
    public final void onPhoto(@NotNull NewMessageEvent e) {
        Message msg = e.getMessage();
        if(msg.getPeerId() != 2000000030) {
            return;
        }
        List<MessageAttachment> attachment = msg.getAttachments();
        if(attachment != null && !attachment.isEmpty()) {
            try {
                BufferedImage image = createResizedCopy(ImageIO.read(attachment.get(0).getPhoto().getSizes().get(1).getUrl()));
                tool.getNeuralReply(PhotoNetwork.photo(image), msg.getPeerId());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private static BufferedImage createResizedCopy(Image originalImage) {
        int imageType = BufferedImage.TYPE_INT_RGB;
        BufferedImage scaledBI = new BufferedImage(100, 100, imageType);
        Graphics2D g = scaledBI.createGraphics();

        g.setComposite(AlphaComposite.Src);

        g.drawImage(originalImage, 0, 0, 100, 100, null);
        g.dispose();
        return scaledBI;
    }
}
