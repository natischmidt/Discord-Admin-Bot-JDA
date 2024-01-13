package events;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class CommandHandler extends ListenerAdapter   {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        switch (Objects.requireNonNull(event.getComponentId())) {
            case "info" -> event.getChannel().sendMessage("This is a simple bot.").queue();
            case "server" -> handleServerCommand(event);
            case "help" -> sendHelpMenu(event.getChannel());
            case "members" -> handleMembersCommand(event);
            case "list" -> handleMemberListCommand(event);
            case "roles" -> handleRolesCommand(event);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        MessageChannel channel = event.getChannel();

        String content = event.getMessage().getContentRaw();

        if (content.equals("/help")) {
            sendHelpMenu(channel);
        }
    }

    private void sendHelpMenu(MessageChannel channel) {
        Map<String, String> commandDescriptions = new HashMap<>();

        commandDescriptions.put("info", "Displays information about the bot.");
        commandDescriptions.put("server", "Displays server information.");
        commandDescriptions.put("members", "Handles members command.");
        commandDescriptions.put("list", "Displays a full list of members.");
        commandDescriptions.put("roles", "Creates a new role.");

        ActionRow actionRow = ActionRow.of(
                Button.primary("info", "Info"),
                Button.primary( "server", "Server"),
                Button.primary( "members", "Members"),
                Button.primary( "list", "List"),
                Button.primary( "roles", "Roles")
        );

        MessageAction messageAction = channel.sendMessage("You can use the following commands:")
                .setActionRows(actionRow)
                .mentionRepliedUser(false);

        for (String command : commandDescriptions.keySet()) {
            messageAction = messageAction.append("\n\n**/").append(command).append(":** ").append(commandDescriptions.get(command));
        }

        messageAction.queue();

    }



    private void handleMemberListCommand( ButtonInteractionEvent event) {
        MessageChannel channel = event.getChannel();
        Objects.requireNonNull(event.getGuild()).getMembers().forEach(member ->
                channel.sendMessage(member.getUser().getName()).queue());
    }

    private void handleServerCommand(@NotNull ButtonInteractionEvent event) {
        if (event.getUser().isBot()) return;

        MessageChannel channel = event.getChannel();
        channel.sendMessage(
                "Servername: " + Objects.requireNonNull(event.getGuild()).getName()
                        + " Description: " + event.getGuild().getDescription()).queue();
    }

    public void handleMembersCommand(ButtonInteractionEvent event) {
        MessageChannel channel = event.getChannel();
        long allMembersCount = Objects.requireNonNull(event.getGuild()).getMembers().size();
        long allBotsCount = event.getGuild().getMembers().stream().
                filter(member -> member.getUser().isBot()).count();

        channel.sendMessage(
                "There are: " + allMembersCount +
                " members in this server, including " + allBotsCount
                + " bots. ").queue();
    }

    public void handleRolesCommand(ButtonInteractionEvent event) {
        if (event.getUser().isBot()) return;

        MessageChannel channel = event.getChannel();
        User user = event.getUser();

        if (Objects.requireNonNull(event.getGuild()).getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
            channel.sendMessage("Please provide the details for the new role.\n" +
                    "Enter the role name:").queue();

            event.getJDA().addEventListener(
                    new RoleCreationListener(user,event)
            );

        } else {
            channel.sendMessage("I don't have the necessary permissions to create roles.").queue();
        }
    }





}
