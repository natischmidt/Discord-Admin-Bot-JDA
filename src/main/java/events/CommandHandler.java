package events;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandHandler extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        switch (command) {
            case "bot-info" -> event.reply("This is a simple bot.").queue();
            case "server-info" -> handleServerCommand(event);
            case "help" -> sendHelpMenu(event);
            case "members" -> handleMembersCommand(event);
            case "ban" -> handleBanCommand(event);
            case "kick" -> handleKickCommand(event);
            case "member-list" -> handleMemberListCommand(event);
            case "create-role" -> handleCreateRolesCommand(event);
            case "list-roles" -> handleListRolesCommand(event);
            case "role-info" -> handleRoleInfoCommand(event);
            case "nickname" -> handleNicknameCommand(event);
            case "create-channel" -> handleCreateChannelCommand(event);
            case "delete-channel" -> handleDeleteChannelCommand(event);
        }
    }

    private void handleServerCommand(@NotNull SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;

        event.deferReply().queue();
        event.getHook().sendMessage(
                "Servername: " + Objects.requireNonNull(event.getGuild()).getName()
                        + " Description: " + event.getGuild().getDescription()).queue();

    }

    private void handleBanCommand(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();

        Member memberToBan = Objects.requireNonNull(event.getOption("user")).getAsMember();

        if (memberToBan != null) {
            memberToBan.ban(7, "Banned by command").queue(
                    success -> event.getHook().editOriginal("User " + memberToBan.getUser().getAsTag() + " has been banned.").queue(),
                    error -> event.getHook().editOriginal("Failed to ban user: " + error.getMessage()).queue()
            );
        } else {
            event.getHook().editOriginal("User not found or not specified.").queue();
        }
    }



    private void handleNicknameCommand(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();

        Member member = Objects.requireNonNull(event.getOption("user")).getAsMember();
        String nickname = Objects.requireNonNull(event.getOption("nickname")).getAsString();

        try {
            assert member != null;
            member.modifyNickname(nickname).queue(
                    success -> event.getHook().editOriginal("Nickname for " + member.getUser().getAsTag() + " set to: " + nickname).queue(),
                    error -> event.getHook().editOriginal("Failed to set nickname: " + error.getMessage()).queue()
            );
        } catch (HierarchyException e) {
            // The bot doesn't have permission to modify the nickname of the specified member
            event.getHook().editOriginal("Error: " + e.getMessage()).queue();
        }
    }


    private void handleKickCommand(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();

        Member memberToKick = Objects.requireNonNull(event.getOption("user")).getAsMember();

        if (memberToKick != null) {
            memberToKick.kick("Kicked by command").queue(
                    success -> event.getHook().editOriginal("User " + memberToKick.getUser().getAsTag() + " has been kicked.").queue(),
                    error -> event.getHook().editOriginal("Failed to kick user: " + error.getMessage()).queue()
            );
        } else {
            event.getHook().editOriginal("User not found or not specified.").queue();
        }
    }
    private void sendHelpMenu(@NotNull SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();

        String helpMenu = """
                You can use the following commands:
                /bot-info - Get information about the bot
                /server-info - Get information about the server
                /help - Show this help menu
                /ban - Ban a member
                /kick - Kick a member
                /members - Get information about the server's members
                /member-list - Get a list of all members in the server
                /create-role - Create a new role with a specified name and color
                /list-roles - Displays a list of all roles in the server
                /role-info - Displays information about a specific role
                /nickname - Set or modify the nickname of a member
                /create-channel - Creates a new text or voice channel with specified settings
                /delete-channel - Deletes the current or specified channel
                """;

        event.getHook().sendMessage(helpMenu).queue();

    }
    public static final Map<String, Integer> colorMap = new HashMap<>();

    static { colorMap.put("red", 0xFF0000); colorMap.put("blue", 0x0000FF); colorMap.put("black", 0x000000); colorMap.put("white", 0xFFFFFF); colorMap.put("yellow", 0xFFFF00); colorMap.put("orange", 0xFFA500); colorMap.put("green", 0x00FF00); colorMap.put("purple", 0x800080); colorMap.put("pink", 0xFFC0CB); colorMap.put("gray", 0x808080); colorMap.put("cyan", 0x00FFFF); colorMap.put("magenta", 0xFF00FF); colorMap.put("gold", 0xFFD700); colorMap.put("silver", 0xC0C0C0); colorMap.put("teal", 0x008080); colorMap.put("navy", 0x000080); colorMap.put("coral", 0xFF7F50); colorMap.put("lime", 0x00FF00); colorMap.put("lavender", 0xE6E6FA); colorMap.put("peach", 0xFFDAB9); colorMap.put("skyblue", 0x87CEEB); colorMap.put("brown", 0xA52A2A); colorMap.put("violet", 0xEE82EE); colorMap.put("indigo", 0x4B0082); }


    public void handleCreateRolesCommand(@NotNull SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();

        String roleName = Objects.requireNonNull(event.getOption("name")).getAsString();
        String colorName = Objects.requireNonNull(event.getOption("color")).getAsString();
        Integer color = colorMap.get(colorName.toLowerCase());

        try {
            if (color != null) {
                Role createdRole =(Objects.requireNonNull(event.getGuild())).createRole()
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

    }

    private void handleListRolesCommand(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();

        List<Role> roles = Objects.requireNonNull(event.getGuild()).getRoles();
        String roleList = roles.stream()
                .map(Role::getName)
                .collect(Collectors.joining(", "));

        event.getHook().editOriginal("List of Roles:\n" + roleList).queue();
    }

    private void handleRoleInfoCommand(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();

        Role role = Objects.requireNonNull(event.getOption("role")).getAsRole();
        String roleInfo = "Role Name: " + role.getName() +
                "\nRole ID: " + role.getId() +
                "\nColor: " + role.getColor() +
                "\nPosition: " + role.getPosition();

        event.getHook().editOriginal(roleInfo).queue();
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

    private void handleCreateChannelCommand(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();

        String channelName = Objects.requireNonNull(event.getOption("name")).getAsString();
        String channelType = Objects.requireNonNull(event.getOption("type")).getAsString();
        ChannelType type = ChannelType.TEXT;

        if (channelType.equalsIgnoreCase("voice")) {
            type = ChannelType.VOICE;
        }
        Guild guild = event.getGuild();
        Category category = event.getOption("category") != null ?
                (Category) Objects.requireNonNull(event.getOption("category")).getAsGuildChannel() : null;

        if (category == null || category.getType() == type) {
            assert guild != null;
            guild.createTextChannel(channelName)
                    .setType(type)
                    .setParent(category)
                    .queue(
                            createdChannel -> event.getHook().editOriginal("New channel created: " + createdChannel.getAsMention()).queue(),
                            error -> event.getHook().editOriginal("Failed to create channel: " + error.getMessage()).queue()
                    );
        } else {
            event.getHook().editOriginal("Channel type and category type must match.").queue();
        }
    }


    private void handleDeleteChannelCommand(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();

        TextChannel channelToDelete = event.getOption("channel") != null ?
                Objects.requireNonNull(event.getOption("channel")).getAsTextChannel() : (TextChannel) event.getChannel();

        if (channelToDelete != null) {
            channelToDelete.delete().queue(
                    success -> event.getHook().editOriginal("Channel deleted: " + channelToDelete.getName()).queue(),
                    error -> event.getHook().editOriginal("Failed to delete channel: " + error.getMessage()).queue()
            );
        } else {
            event.getHook().editOriginal("Channel not found or not specified.").queue();
        }
    }

}




