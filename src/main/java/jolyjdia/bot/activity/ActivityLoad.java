package jolyjdia.bot.activity;

import api.command.defaults.HappyCommand;
import api.event.EventLabel;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.module.Module;
import api.utils.VkUtils;
import api.utils.timeformat.TemporalDuration;
import api.utils.timeformat.TimeFormatter;
import com.google.common.collect.ImmutableMap;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import jolyjdia.bot.Bot;
import jolyjdia.bot.utils.PhotoTool;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

public class ActivityLoad implements Module, Listener {
    private static final Map<String, String> ACTIVITIES = ImmutableMap.<String, String>builder()
            .put("idea64.exe", "\uD83D\uDCBBCoding in IntelliJ IDEA(среда разработки)")
            .put("Discord.exe", "✅Онлайн в Дискорде")
            .put("Telegram.exe", "✅Онлайн в Телеграме")
            .build();
    private boolean loop;

    @Override
    public final void onLoad() {
        Bot.getBotManager().registerEvent(this);
        Bot.getScheduler().scheduleSyncRepeatingTask(() -> {
            try {
                Bot.getVkApiClient().status()
                        .set(VkUtils.USER_ACTOR)
                        .text(this.loop ?
                                "1.15 релиз: "+TemporalDuration.of(12, 11, 0,0)
                                        .toFormat(TimeFormatter.DAYS,
                                                TimeFormatter.HOURS,
                                                TimeFormatter.MINUTES) :
                              //  String.format(HappyCommand.NEW_YEAR, getNewYearInStatus()) :
                                getApplications())
                        .execute();
                this.loop = !loop;
                Bot.getVkApiClient().status()
                        .set(VkUtils.USER_ACTOR)
                        .groupId(Bot.getGroupId())
                        .text(String.format(HappyCommand.NEW_YEAR, getNewYearInStatus()))
                        .execute();
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
        }, 0, 2000);
    }

    @NonNls
    public static @NotNull String getApplications() {
        StringBuilder builder = ProcessHandle.allProcesses()
                .map(ProcessHandle::info)
                .map(ProcessHandle.Info::command)
                .distinct()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Paths::get)
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(ACTIVITIES::containsKey)
                .limit(3)
                .collect(StringBuilder::new,
                        (set, item) -> set.append(ACTIVITIES.get(item)).append(", "),
                        StringBuilder::append);
        return "Сейчас: " + builder.substring(0, builder.length()-2);
    }
    public static @NotNull String getNewYearInStatus() {
        return TemporalDuration.of(1, 1, 0,0).toFormat(TimeFormatter.DAYS, TimeFormatter.HOURS, TimeFormatter.MINUTES);
    }

    @EventLabel
    public static void onMsg(NewMessageEvent e) {
        String text = e.getMessage().getText();
        if(text.isEmpty()) {
            return;
        }
        if(text.startsWith("```") && text.endsWith("```")) {
            text = text.substring(3);
            text = text.substring(0, text.length()-3);
            BufferedImage image = convertTextToGraphic(text, new Font("Consolas", Font.BOLD, 18));
            try {
                ImageIO.write(image, "png", new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\test.png"));
                PhotoTool.sendPhoto(new File("D:\\IdeaProjects\\VkBot\\src\\main\\resources\\test.png"), e.getUser().getPeerId());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static String styleCode(String text) {
        int c = -3;
        StringBuilder builder = new StringBuilder();
        for(String s1 : text.split("\n")) {
            s1 = s1.substring(0, Math.max(0, s1.length()-1));
            if (s1.endsWith("{")) {
                c += 3;
                builder.append('\n').append(" ".repeat(Math.max(0, c))).append(s1);
            } else if (s1.endsWith("}") || s1.startsWith("}")) {
                c -= 3;
                builder.append('\n').append(" ".repeat(Math.max(0, c))).append(s1);
            } else {
                builder.append('\n').append(" ".repeat(Math.max(0, c+3))).append(s1);
            }
        }
        return builder.toString();
    }
    public static String longLine(String s) {
        int l = 0;
        String end = "";
        for(String s1 : s.split("\n")) {
            if(s1.length() > l) {
                l = s1.length();
                end = s1;
            }
        }
        return end;
    }
    public static BufferedImage convertTextToGraphic(String text, Font font) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        text = styleCode(text);
        int width = fm.stringWidth(longLine(text));
        int height = text.split("\n").length*(fm.getAscent()+15);
        g2d.dispose();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        graphics.setFont(font);
        FontMetrics fontMetrics = graphics.getFontMetrics();
        graphics.setColor(Color.DARK_GRAY);
        int ascent = fontMetrics.getAscent()+15;
        int space = 0;
        for(String line : text.split("\n")) {
            graphics.drawString(line, 0, space);
            space += ascent;
        }
        graphics.dispose();
        return bufferedImage;
    }
}
