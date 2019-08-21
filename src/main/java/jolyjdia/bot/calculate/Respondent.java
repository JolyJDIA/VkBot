package jolyjdia.bot.calculate;

import api.JavaModule;
import api.event.EventHandler;
import api.event.Listener;
import api.event.RegisterListEvent;
import api.event.messages.NewMessageEvent;
import api.utils.ObedientBot;
import com.vk.api.sdk.objects.messages.Message;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class Respondent extends JavaModule implements Listener {

	private static final Pattern MATH = Pattern.compile("[a-zA-Z\\d+\\-*/()^< ]*");

	@Override
	public final void onLoad() {
        RegisterListEvent.registerEvent(this);
    }
	@EventHandler
	public static void onSend(@NotNull NewMessageEvent e) {
		Message msg = e.getMessage();
		if(MATH.matcher(msg.getText()).matches()) {
			String answer = new Calculator(msg.getText()).evaluate();
			if(answer.isEmpty()) {
			    return;
            }
			ObedientBot.sendMessage(answer, msg.getPeerId());
		}
	}
}
