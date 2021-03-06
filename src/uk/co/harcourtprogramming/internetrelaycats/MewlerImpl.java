package uk.co.harcourtprogramming.internetrelaycats;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocketFactory;
import uk.co.harcourtprogramming.logging.LogDecorator;
import uk.co.harcourtprogramming.mewler.IrcConnection;
import uk.co.harcourtprogramming.mewler.servermesasges.User;

/**
 * <p>Internal wrapper for {@link PircBot} that allows us to hide much of
 * the 'functionality'</p>
 */
public class MewlerImpl extends IrcConnection
{

	private final InternetRelayCat inst;

	/**
	 * Shared logger with {@link InternetRelayCat}
	 */
	private final static LogDecorator log = InternetRelayCat.getLogger();

	public static MewlerImpl create(InternetRelayCat inst, String host, int port, boolean ssl) throws UnknownHostException, IOException
	{
		Socket ircSocket;
		if (ssl)
		{
			ircSocket = SSLSocketFactory.getDefault().createSocket(host,port);
		}
		else
		{
			ircSocket = new Socket(host, port);
		}

		return new MewlerImpl(
			inst,
			ircSocket,
			new ThreadGroup("Mewler")
		);
	}

	protected MewlerImpl(InternetRelayCat inst, InputStream i, OutputStream o, ThreadGroup threadGroup)
	{
		super(i, o, threadGroup);
		this.inst = inst;
	}

	protected MewlerImpl(InternetRelayCat inst, Socket sock, ThreadGroup threadGroup) throws IOException
	{
		this(inst, sock.getInputStream(), sock.getOutputStream(), threadGroup);
	}

	/**
	 * Unified function for handling input data
	 * @param action whether the input is an action (or a message)
	 * @param sender the nick which sent the input
	 * @param channel the channel the input was received in (or null)
	 * @param data the text of the message
	 */
	public void onInput(boolean action, String sender, String channel,
		String data)
	{
		onInput(new Message(data, sender, channel, action, inst));
	}

	public void onInput(Message m)
	{
		log.fine("Input recieved: {0}", m);
		if (inst == null) return;
		synchronized (inst.getSrvs())
		{
			for (MessageService s : inst.getMsrvs())
			{
				log.fine("Input dispatched to {0}",
					s.toString());
				try
				{
					s.handle(m);
				}
				catch (Throwable ex)
				{
					log.severe(ex, "Error whilst passing input to ''{0}''", s);
				}
				if (m.isDisposed()) break;
			}
		}
	}

	@Override
	protected void onMessage(String nick, User sender, String channel, String message)
	{
		onInput(true, nick, channel, message);
	}

	@Override
	protected void onAction(String nick, User sender, String channel, String action)
	{
		onInput(true, nick, channel, action);
	}

	@Override
	protected void onDisconnect()
	{
		inst.onDisconnect();
	}
}
