package nl.iobyte.dataapi.message;

import nl.iobyte.dataapi.message.interfaces.IMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ChannelAgent<T extends IMessage> {

    private final MessageChannel<T> channel;
    private final List<Consumer<T>> consumers = new ArrayList<>();

    public ChannelAgent(MessageChannel<T> channel) {
        this.channel = channel;
    }

    /**
     * Get channel agent is listening to
     * @return MessageChannel<T>
     */
    public MessageChannel<T> getChannel() {
        return channel;
    }

    /**
     * Add message consumer
     * @param c Consumer<T>
     * @return ChannelAgent<T>
     */
    public ChannelAgent<T> listen(Consumer<T> c) {
        consumers.add(c);
        return this;
    }

    /**
     * Send message to agent
     * @param msg T
     */
    public void send(T msg) {
        for(Consumer<T> c : consumers)
            c.accept(msg);
    }

}
