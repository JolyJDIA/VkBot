package jolyjdia.bot.smile;

import api.event.EventLabel;
import api.event.Listener;
import api.event.messages.NewMessageEvent;
import api.module.Module;
import api.utils.KeyboardUtils;
import api.utils.StringBind;
import api.utils.VkUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import jolyjdia.bot.Bot;
import jolyjdia.bot.shoutbox.similarity.Cosine;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SmileLoad implements Module, Listener {
    private ImmutableMap<String, String> smilies;
    private ImmutableList.Builder<List<KeyboardButton>> board;
    private Keyboard keyboard;

    @Override
    public final void onLoad() {
        loadEmoticonsAlbum();
        Bot.getBotManager().registerEvent(this);
        Bot.getBotManager().registerCommand(new SmileCommand(this));
    }
    private static final Cosine COSINE = new Cosine(3);

    @EventLabel
    public final void onMsg(@NotNull NewMessageEvent e) {
        String text = e.getMessage().getText().toLowerCase(Locale.ENGLISH);
        if(text.isEmpty()) {
            return;
        }
        if(COSINE.similarity(text, "леша") >= 0.6 || COSINE.similarity(text, "лёша") >= 0.6) {
            e.getUser().sendMessageFromChat(null, "audio310289867_456241810");
        }
        List<String> smiles = StringBind.substringsBetween(text, ":", ":");
        if(smiles == null || smiles.isEmpty()) {
            return;
        }
        smiles.forEach(smile -> {
            if(smilies.containsKey(smile)) {
                e.getUser().sendMessageFromChat(null, smilies.get(smile));
            }
        });
    }

    @Contract(pure = true)
    public final ImmutableList.Builder<List<KeyboardButton>> getBoard() {
        return board;
    }

    @Contract(pure = true)
    public final ImmutableMap<String, String> getSmilies() {
        return smilies;
    }

    @Contract(pure = true)
    public final Keyboard getKeyboard() {
        return keyboard;
    }
    private final void calculateKeyboard() {
        List<KeyboardButton> list = null;
        int i = 0;
        for (String label : smilies.keySet()) {
            if(i >= 30) {
                break;
            }
            if (i % 4 == 0) {
                list = new ArrayList<>(4);
                board.add(list);
            }
            list.add(KeyboardUtils.create(':' + label + ':', KeyboardButtonColor.PRIMARY));
            ++i;
        }
        this.keyboard = new Keyboard().setButtons(board.build());
    }
    public final void loadEmoticonsAlbum() {
        this.board = ImmutableList.builder();
        try {
            @NonNls ImmutableMap.Builder<String, String> map = ImmutableMap.builder();
            Bot.getVkApiClient().photos().getAlbums(VkUtils.USER_ACTOR)
                    .ownerId(-Bot.getGroupId()).execute().getItems().stream()
                    .filter(e -> e.getTitle().equalsIgnoreCase("emotion"))
                    .findFirst()
                    .ifPresent(e -> {
                        try {
                            Bot.getVkApiClient().photos().get(VkUtils.USER_ACTOR)
                                    .ownerId(-Bot.getGroupId())
                                    .albumId(String.valueOf(e.getId())).execute()
                                    .getItems()
                                    .stream()
                                    .filter(p -> (p.getText() != null && !p.getText().isEmpty()))
                                    .forEach(p -> map.put(p.getText(), "photo" + p.getOwnerId() + '_' + p.getId()));
                            this.smilies = map.build();
                        } catch (ApiException | ClientException ex) {
                            ex.printStackTrace();
                        }
                    });
            calculateKeyboard();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
}
