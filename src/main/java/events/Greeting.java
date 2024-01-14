package events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class Greeting extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw();

        if (content.equalsIgnoreCase("hi lexi")) {
            String name = Objects.requireNonNull(event.getMember()).getEffectiveName();
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Hi " + name ).queue();
        }
    }
}


