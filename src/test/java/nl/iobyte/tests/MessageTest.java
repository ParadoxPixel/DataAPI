package nl.iobyte.tests;

import nl.iobyte.dataapi.message.ChannelAgent;
import nl.iobyte.dataapi.message.MessageBroker;
import nl.iobyte.dataapi.message.interfaces.IMessage;
import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageTest {

    @Test
    public void test() {
        MessageBroker broker = new MessageBroker();

        AtomicBoolean goodCall = new AtomicBoolean(false);
        broker.getOptional(
                "player.action.join",
                TestJoinMessage.class
        ).ifPresentOrElse(channel -> {
            ChannelAgent<TestJoinMessage> agent = channel.newAgent();

            agent.listen(msg -> goodCall.set(true));
        }, () -> Assert.fail("Channel should not be null"));

        //Test specific path
        broker.send(new TestJoinMessage(), "player.action.join");
        Assert.assertTrue("Listener of agent not called", goodCall.get());

        //Test wildcard
        goodCall.set(false);
        broker.send(new TestJoinMessage(), "player.action.*");
        Assert.assertTrue("Listener of agent not called", goodCall.get());
    }

    public static class TestJoinMessage implements IMessage {

    }

}
