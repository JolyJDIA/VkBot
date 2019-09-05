package jolyjdia.bot.calculate;

import api.JavaModule;
import api.event.RegisterListEvent;

public class CalculatorRegister extends JavaModule {

	@Override
	public final void onLoad() {
        RegisterListEvent.registerEvent(new CalculatorListener());
	//	RegisterCommandList.registerCommand(new CalculatorCommand());
    }
}
