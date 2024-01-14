package events;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AutoCompleteBot extends ListenerAdapter {
    private static final Map<String, Integer> colorMap = CommandHandler.colorMap;

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (event.getName().equals("create-role") && event.getFocusedOption().getName().equals("color")) {
            List<Command.Choice> options = colorMap.keySet().stream()
                    .filter(color -> color.startsWith(event.getFocusedOption().getValue()))
                    .map(color -> new Command.Choice(color, color))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }
}