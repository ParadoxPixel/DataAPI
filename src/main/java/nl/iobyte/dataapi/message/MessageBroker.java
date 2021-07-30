package nl.iobyte.dataapi.message;

import nl.iobyte.dataapi.message.interfaces.IMessage;
import nl.iobyte.dataapi.namespace.NamespaceMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MessageBroker extends NamespaceMap<MessageChannel<?>> {

    /**
     * Get channel and add if not exists
     * @param name String
     * @param clazz Class<T>
     * @param <T> T
     * @return MessageChannel<T>
     */
    @SuppressWarnings("unchecked")
    public <T extends IMessage> MessageChannel<T> get(String name, Class<T> clazz) {
        if(name.contains("*") || clazz == null)
            return null;

        MessageChannel<?> c = first(name);
        if(c == null) {
            MessageChannel<T> channel = new MessageChannel<>(clazz, name);
            set(name, channel);
            return channel;
        }

        if(c.getType() != clazz)
            return null;

        return (MessageChannel<T>) c;
    }

    /**
     * Get optional channel and add if not exists
     * @param name String
     * @param clazz Class<T>
     * @param <T> T
     * @return Optional<MessageChannel<T>>
     */
    public <T extends IMessage> Optional<MessageChannel<T>> getOptional(String name, Class<T> clazz) {
        return Optional.ofNullable(get(name, clazz));
    }

    /**
     * Send message to channels
     * @param msg IMessage
     * @param paths String[]
     */
    public void send(IMessage msg, String... paths) {
        //Remove duplicate keys from path
        paths = new HashSet<>(Arrays.asList(paths)).toArray(new String[0]);

        //Gather channels
        Set<MessageChannel<?>> channels = new HashSet<>();
        for(String path : paths)
            channels.addAll(get(path));

        //Send message to channels
        for(MessageChannel<?> channel : channels) {
            if(!channel.getType().isAssignableFrom(msg.getClass()))
                continue;

            channel.sendRaw(msg);
        }
    }

}
