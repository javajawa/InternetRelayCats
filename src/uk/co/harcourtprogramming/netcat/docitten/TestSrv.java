package uk.co.harcourtprogramming.netcat.docitten;

import java.util.logging.Level;
import uk.co.harcourtprogramming.netcat.MessageService;
import uk.co.harcourtprogramming.netcat.NetCat.Message;

public class TestSrv extends MessageService
{
	public TestSrv()
	{
		// Nothing to see here. Move along, citizen!
		log(Level.INFO, "TestSrv Starting up");
	}

	public void handle(Message m)
	{
		log(Level.INFO, m.getSender() + '\t' + m.getChannel() + '\n' + m.getMessage().substring(0,Math.min(20,m.getMessage().length())));
	}

	public void shutdown()
	{
		// Nothing to see here. Move along, citizen!
		log(Level.INFO, "TestSrv shutting down");
	}
}

