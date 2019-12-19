package api.command.defaults;

import api.command.Command;
import api.storage.User;
import api.utils.StringBind;
import api.utils.VkUtils;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import jolyjdia.bot.Bot;

public class ZavrCommand extends Command {
    public ZavrCommand() {
        super("wallpost");
    }

    @Override
    public final void execute(User sender, String[] args) {
        try {
            Bot.getVkApiClient().wall()
                    .post(VkUtils.USER_ACTOR)
                    .message(StringBind.toString(args))
                    .ownerId(VkUtils.USER_ACTOR.getId())
                    .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
}
