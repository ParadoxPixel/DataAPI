package nl.iobyte.dataapi.message;

import nl.iobyte.dataapi.message.interfaces.IMessage;
import java.util.ArrayList;
import java.util.List;

public class MessageChannel<T extends IMessage> {

    private final Class<T> clazz;
    private final String name;
    private final List<ChannelAgent<T>> agents = new ArrayList<>();

    public MessageChannel(Class<T> clazz, String name) {
        this.clazz = clazz;
        this.name = name;
    }

    /**
     * Get type of channel
     * @return Class<T>
     */
    public Class<T> getType() {
        return clazz;
    }

    /**
     * Get name of channel
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Create new agent
     * @return ChannelAgent<T>
     */
    public ChannelAgent<T> newAgent() {
        ChannelAgent<T> agent = new ChannelAgent<>(this);
        agents.add(agent);
        return agent;
    }

    /**
     * Send message to agents
     * @param msg T
     */
    public void send(T msg) {
        for(ChannelAgent<T> agent : agents)
            agent.send(msg);
    }

    /**
     * Cast IMessage to type T and send
     * @param msg IMessage
     */
    @SuppressWarnings("unchecked")
    public void sendRaw(IMessage msg) {
        T obj;
        try {
            obj = (T) msg;
        } catch (Exception e) {
            return;
        }

        send(obj);
    }

}
