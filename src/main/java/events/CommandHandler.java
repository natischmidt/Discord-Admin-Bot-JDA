package events;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class CommandHandler extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String content = event.getMessage().getContentRaw();

        if (content.startsWith("/")) {
            handleCommand(content.substring(1), event);
        }
    }

    public void handleCommand(String command, MessageReceivedEvent event) {
        switch (command.toLowerCase()) {
            case "help" -> event.getChannel().sendMessage
                    ("""
                            You can use following commands:
                            /help  show all commands
                            /info  show info about Lexi\s
                            /server display server info\s
                            /members shows members\s
                            /list  to display a full list of members
                            /roles to create a new role
                            \s""").queue();
            case "info" -> event.getChannel().sendMessage("This is a simple bot.").queue();
            case "server" -> handleServerCommand(event);
            case "members" -> handleMembersCommand(event);
            case "list" -> handleMemberListCommand(event);
            case "roles" -> handleRolesCommand(event);
            default -> event.getChannel().sendMessage("Unknown command.").queue();
        }
    }

    private void handleMemberListCommand(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        event.getGuild().getMembers().forEach(member ->
                channel.sendMessage(member.getUser().getName()).queue());
    }

    public void handleServerCommand(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        MessageChannel channel = event.getChannel();
        channel.sendMessage(
                " Servername: " + event.getGuild().getName()
                +    " Description: " + event.getGuild().getDescription()).queue();
    }

    public void handleMembersCommand(@NotNull MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        long allMembersCount = event.getGuild().getMembers().size();
        long allBotsCount = event.getGuild().getMembers().stream().
                filter(member -> member.getUser().isBot()).count();

        channel.sendMessage(
                "There are: " + allMembersCount +
                " members in this server, including " + allBotsCount
                + " bots. ").queue();
    }



    public void handleRolesCommand(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        MessageChannel channel = event.getChannel();
        User user = event.getAuthor();

        if (event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {

            channel.sendMessage("Please provide the details for the new role.\n" +
                    "Enter the role name:").queue();

            event.getJDA().addEventListener(new RoleCreationListener(user, roleName -> {
                channel.sendMessage("Role name provided: " + roleName).queue();
            }, event));
        } else {
            channel.sendMessage("I don't have the necessary permissions to create roles.").queue();
        }
    }


}
