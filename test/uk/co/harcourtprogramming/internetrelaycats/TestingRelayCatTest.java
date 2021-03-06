package uk.co.harcourtprogramming.internetrelaycats;

import java.io.IOException;
import java.net.UnknownHostException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Benedict
 */
public class TestingRelayCatTest
{

	/**
	 *
	 */
	@SuppressWarnings("ResultOfObjectAllocationIgnored")
	public void testCreation() throws UnknownHostException, IOException
	{
		new TestingRelayCat();
	}

	/**
	 * Test of run method, of class TestingRelayCat.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testRun() throws UnknownHostException, IOException
	{
		TestingRelayCat instance = new TestingRelayCat();
		instance.run();
	}

	/**
	 * Test of message method, of class TestingRelayCat.
	 */
	@Test
	public void testMessage() throws UnknownHostException, IOException
	{
		final String target = "bob";
		final String message = "hello";
		final TestingRelayCat instance = new TestingRelayCat();

		instance.message(target, message);
		Message m = instance.getOutput();

		assertNotNull(m);
		assertFalse("Message is an action", m.isAction());
		assertEquals("Message target incorrect", m.getChannel(), target);
		assertEquals("Message data incorrect", m.getMessage(), message);

		m = instance.getOutput();
		assertNull("More than one action generated by message()", m);
	}

	/**
	 * Test of act method, of class TestingRelayCat.
	 */
	@Test
	public void testAct() throws UnknownHostException, IOException
	{
		final String target = "bob";
		final String message = "hello";
		final TestingRelayCat instance = new TestingRelayCat();

		instance.act(target, message);
		Message m = instance.getOutput();

		assertNotNull(m);
		assertTrue("Message was not an action", m.isAction());
		assertEquals("Message target incorrect", m.getChannel(), target);
		assertEquals("Message data incorrect", m.getMessage(), message);

		m = instance.getOutput();
		assertNull("More than one action generated by act()", m);
	}

	/**
	 *
	 */
	@Test
	public void testInputMessageWithChannel() throws UnknownHostException, IOException
	{
		final TestingRelayCat instance = new TestingRelayCat();
		instance.addService(new TestingRelayCat.RelayService());

		final String sender = "bob";
		final String channel = "#irc";
		final String message = "hello";

		Message m = null;

		instance.inputMessage(sender, channel, message);

		// For a channel input, should reply to user then channel
		m = instance.getOutput();
		assertNotNull("No reply (1) sent to sender by relay service", m);
		assertEquals("Reply 1 not sent to sender", m.getChannel(), sender);
		assertEquals("Reply 1 Message data incorrect", m.getMessage(), message);

		m = instance.getOutput();
		assertNotNull("No reply (2) sent to channel by relay service", m);
		assertEquals("Reply 2 not sent to sender", m.getChannel(), channel);
		assertEquals("Reply 2 Message data incorrect", m.getMessage(), message);

		m = instance.getOutput();
		assertNull("More than two replies sent by relay service", m);
	}

	/**
	 *
	 */
	@Test
	public void testInputMessageWithoutChannel() throws UnknownHostException, IOException
	{
		final TestingRelayCat instance = new TestingRelayCat();
		instance.addService(new TestingRelayCat.RelayService());

		final String sender = "bob";
		final String channel = null;
		final String message = "hello";

		Message m = null;

		instance.inputMessage(sender, channel, message);

		// For a channel input, should reply to only the user
		m = instance.getOutput();
		assertNotNull("No reply sent to sender by relay service", m);
		assertEquals("Reply not sent to sender", m.getChannel(), sender);
		assertEquals("Reply Message data incorrect", m.getMessage(), message);

		m = instance.getOutput();
		assertNull("More than one reply sent by relay service", m);
	}

	/**
	 *
	 */
	@Test
	public void testGetOutputWithoutInput() throws UnknownHostException, IOException
	{
		TestingRelayCat instance = new TestingRelayCat();
		assertNull(instance.getOutput());
	}

}
