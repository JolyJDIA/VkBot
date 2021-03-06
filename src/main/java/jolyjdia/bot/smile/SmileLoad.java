package jolyjdia.bot.smile;

import com.google.common.collect.Lists;
import jolyjdia.api.event.EventLabel;
import jolyjdia.api.event.Listener;
import jolyjdia.api.event.messages.NewMessageEvent;
import jolyjdia.api.module.Module;
import jolyjdia.api.utils.KeyboardUtils;
import jolyjdia.api.utils.VkUtils;
import jolyjdia.bot.Bot;
import jolyjdia.vk.api.exceptions.ApiException;
import jolyjdia.vk.api.exceptions.ClientException;
import jolyjdia.vk.api.objects.messages.Keyboard;
import jolyjdia.vk.api.objects.messages.KeyboardButton;
import jolyjdia.vk.api.objects.messages.KeyboardButtonColor;
import jolyjdia.vk.api.objects.photos.Photo;
import jolyjdia.vk.api.objects.photos.PhotoAlbumFull;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class SmileLoad implements Module, Listener {
    private final Map<String, String> smilies = new HashMap<>();
    private final List<List<KeyboardButton>> board = new ArrayList<>();
    private final Keyboard keyboard = new Keyboard();
    private static SmileLoad ourInstance;

    @Override
    public final void onLoad() {
        ourInstance = this;
        loadEmoticonsAlbum();
        Bot.getBotManager().registerEvent(this);
        Bot.getBotManager().registerCommand(new SmileCommand(this));
    }
    public static SmileLoad getInstance() {
        return ourInstance;
    }

    private static final Pattern COMPILE = Pattern.compile(":[A-Za-z_0-9]+:");

    @EventLabel
    public final void onMsg(@NotNull NewMessageEvent e) {
        String text = e.getMessage().getText().toLowerCase(Locale.ENGLISH);
        if(text.isEmpty() || !text.contains(":")) {
            return;
        }
        COMPILE.matcher(text).results().limit(3).map(g -> {
            String smile = g.group();
            smile = smile.substring(1);
            return smile.substring(0, smile.length()-1);
        }).forEach(s -> {
            if(smilies.containsKey(s)) {
                e.getChat().sendAttachments(smilies.get(s));
            }
        });
    }

    public final List<List<KeyboardButton>> getBoard() {
        return board;
    }

    public final Map<String, String> getSmilies() {
        return smilies;
    }

    public final Keyboard getKeyboard() {
        return keyboard;
    }
    private void calculateKeyboard() {
        List<KeyboardButton> list = null;
        int i = 0;
        for (String label : smilies.keySet()) {
            if(i >= 30) {
                break;
            }
            if (i % 5 == 0) {
                list = new ArrayList<>(4);
                board.add(list);
            }
            list.add(KeyboardUtils.create(':' + label + ':', KeyboardButtonColor.PRIMARY));
            ++i;
        }
        this.keyboard.setButtons(board);
    }
    private int albumId;
    public final void loadEmoticonsAlbum() {
        try {
            List<PhotoAlbumFull> albumFulls = Objects.requireNonNull(Bot.getVkApiClient().photos().getAlbums(VkUtils.ZAVR)
                    .ownerId(-Bot.getGroupId())
                    .execute())
                    .getItems();
            if(albumFulls.stream().anyMatch(e -> {
                if(!e.getTitle().equalsIgnoreCase("emotion")) {
                    return false;
                }
                this.albumId = e.getId();
                try {
                    Objects.requireNonNull(Bot.getVkApiClient().photos().get(VkUtils.ZAVR)
                            .ownerId(-Bot.getGroupId())
                            .albumId(String.valueOf(e.getId())).execute())
                            .getItems()
                            .forEach(p -> {
                                @NonNls String label = p.getText();
                                if(label.isEmpty()) {
                                    label = "not_found"+smilies.size();
                                }
                                smilies.put(label, VkUtils.attachment(p));
                            });
                    return true;
                } catch (ApiException | ClientException ex) { return false; }
            })) {
                calculateKeyboard();
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
    public final void addSmile(@NotNull Photo photo) {
        List<KeyboardButton> list = board.get(board.size()-1);
        String label = photo.getText().isEmpty() ? "not_found"+list.size() : photo.getText();

        smilies.put(label, VkUtils.attachment(photo));

        if(list.size() >= 5) {
            board.add(Lists.newArrayList(KeyboardUtils.create(':' + label + ':', KeyboardButtonColor.PRIMARY)));
        } else {
            list.add(KeyboardUtils.create(':' + label + ':', KeyboardButtonColor.PRIMARY));
        }
        this.keyboard.setButtons(board);
    }

    public int getAlbumId() {
        return albumId;
    }
}
