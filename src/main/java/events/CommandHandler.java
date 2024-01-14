package events;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandHandler extends ListenerAdapter   {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        switch (command) {
            case "info" -> event.reply("This is a simple bot.").queue();
            case "server" -> handleServerCommand(event);
            case "help" -> sendHelpMenu(event);
            case "members" -> handleMembersCommand(event);
            case "memberlist" -> handleMemberListCommand(event);
            case "roles" -> handleCreateRoleCommand(event);
        }
    }

    private void handleServerCommand(@NotNull SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;

       event.deferReply().queue();
       event.getHook().sendMessage(
               "Servername: " + Objects.requireNonNull(event.getGuild()).getName()
               + " Description: " + event.getGuild().getDescription()).queue();

    }

    private void sendHelpMenu(@NotNull SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();
        event.getHook().sendMessage("You can use the following commands").queue();

    }

    public void handleMembersCommand(@NotNull SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();

        long allMembersCount = Objects.requireNonNull(event.getGuild()).getMembers().size();
        long allBotsCount = event.getGuild().getMembers().stream().
                filter(member -> member.getUser().isBot()).count();

        event.getHook().sendMessage("There are: " + allMembersCount +
                " members in this server, including " + allBotsCount
                + " bots. ").queue();
    }

    private void handleMemberListCommand(@NotNull SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;

        event.deferReply().queue();
        StringBuilder membersList = new StringBuilder();

        Objects.requireNonNull(event.getGuild()).getMembers().forEach(member ->
                membersList.append(member.getUser().getName()).append("\n")
        );

        event.getHook().sendMessage("List of Members:\n" + membersList).queue();
    }

//    public void handleRolesCommand(@NotNull SlashCommandInteractionEvent event) {
//        event.deferReply().queue();
//
//        if ((Objects.requireNonNull(event.getGuild())).getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
//            event.getHook().sendMessage("Please provide the details for the new role.\n" +
//                    "Enter the role name:").queue();
//
//            event.getJDA().addEventListener(new RoleCreationListener(event));
//        } else {
//            event.getHook().sendMessage("I don't have the necessary permissions to create roles.").queue();
//        }
//    }
//
private static final Map<String, Integer> colorMap = new HashMap<>();

    static {
        colorMap.put("red", 0xFF0000);
        colorMap.put("blue", 0x0000FF);
        colorMap.put("black", 0x000000);
        colorMap.put("white", 0xFFFFFF);
        colorMap.put("yellow", 0xFFFF00);
        colorMap.put("orange", 0xFFA500);
        colorMap.put("green", 0x00FF00);
        colorMap.put("purple", 0x800080);
        colorMap.put("pink", 0xFFC0CB);
        colorMap.put("brown", 0xA52A2A);
        colorMap.put("gray", 0x808080);
        colorMap.put("cyan", 0x00FFFF);
        colorMap.put("magenta", 0xFF00FF);
        colorMap.put("violet", 0xEE82EE);
        colorMap.put("indigo", 0x4B0082);
        colorMap.put("gold", 0xFFD700);
        colorMap.put("silver", 0xC0C0C0);
        colorMap.put("olive", 0x808000);
        colorMap.put("teal", 0x008080);
        colorMap.put("navy", 0x000080);
        colorMap.put("coral", 0xFF7F50);
        colorMap.put("lime", 0x00FF00);
        colorMap.put("lavender", 0xE6E6FA);
        colorMap.put("peach", 0xFFDAB9);
        colorMap.put("salmon", 0xFA8072);
        colorMap.put("skyblue", 0x87CEEB);
    }

    private void handleCreateRoleCommand(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        String roleName = event.getOption("name").getAsString();
        String colorName = event.getOption("color").getAsString();
        Integer color = colorMap.get(colorName.toLowerCase());

        try {
            if (color != null) {
                Role createdRole = Objects.requireNonNull(event.getGuild()).createRole()
                        .setName(roleName)
                        .setColor(color)
                        .setHoisted(true)
                        .complete();

                event.getHook().editOriginal("New role created: " + createdRole.getName()).queue();
            } else {
                String validColorNames = String.join(", ", colorMap.keySet());
                event.getHook().editOriginal("Invalid color name. Please provide a valid color name. " +
                        "Valid names: " + validColorNames).queue();
            }
        } catch (Exception e) {
            event.getHook().editOriginal("An error occurred while creating the role.").queue();
            throw new RuntimeException(e);
        }
    }}



