package events;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
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
            case "info" -> event.reply("This is a simple bot.").queue();
            case "server" -> handleServerCommand(event);
            case "help" -> sendHelpMenu(event);
            case "members" -> handleMembersCommand(event);
            case "ban" -> handleBanCommand(event);
            case "kick" -> handleKickCommand(event);
            case "member-list" -> handleMemberListCommand(event);
            case "create-role" -> handleCreateRolesCommand(event);
            case "list-roles" -> handleListRolesCommand(event);
            case "role-info" -> handleRoleInfoCommand(event);
            case "nickname" -> handleNicknameCommand(event);
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

        Member memberToBan = event.getOption("user").getAsMember();

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

        Member member = event.getOption("user").getAsMember();
        String nickname = event.getOption("nickname").getAsString();

        if (member != null) {
            member.modifyNickname(nickname).queue(
                    success -> event.getHook().editOriginal("Nickname for " + member.getUser().getAsTag() + " set to: " + nickname).queue(),
                    error -> event.getHook().editOriginal("Failed to set nickname: " + error.getMessage()).queue()
            );
        } else {
            event.getHook().editOriginal("Member not found or not specified.").queue();
        }
    }

    private void handleKickCommand(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();

        Member memberToKick = event.getOption("user").getAsMember();

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

        String helpMenu = "You can use the following commands:\n" +
                "/info - Get information about the bot\n" +
                "/server - Get information about the server\n" +
                "/help - Show this help menu\n" +
                "/ban - Ban a member\n" +
                "/kick - Kick a member\n" +
                "/members - Get information about the server's members\n" +
                "/member-list - Get a list of all members in the server\n" +
                "/create-role - Create a new role with a specified name and color\n" +
                "/list-roles - Displays a list of all roles in the server\n" +
                "/role-info - Displays information about a specific role\n" +
                "/nickname - Set or modify the nickname of a member\n";

        event.getHook().sendMessage(helpMenu).queue();

    }
    public static final Map<String, Integer> colorMap = new HashMap<>();

    static { colorMap.put("red", 0xFF0000); colorMap.put("blue", 0x0000FF); colorMap.put("black", 0x000000); colorMap.put("white", 0xFFFFFF); colorMap.put("yellow", 0xFFFF00); colorMap.put("orange", 0xFFA500); colorMap.put("green", 0x00FF00); colorMap.put("purple", 0x800080); colorMap.put("pink", 0xFFC0CB); colorMap.put("gray", 0x808080); colorMap.put("cyan", 0x00FFFF); colorMap.put("magenta", 0xFF00FF); colorMap.put("gold", 0xFFD700); colorMap.put("silver", 0xC0C0C0); colorMap.put("teal", 0x008080); colorMap.put("navy", 0x000080); colorMap.put("coral", 0xFF7F50); colorMap.put("lime", 0x00FF00); colorMap.put("lavender", 0xE6E6FA); colorMap.put("peach", 0xFFDAB9); colorMap.put("skyblue", 0x87CEEB); colorMap.put("brown", 0xA52A2A); colorMap.put("violet", 0xEE82EE); colorMap.put("indigo", 0x4B0082); }


    public void handleCreateRolesCommand(@NotNull SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();

        String roleName = event.getOption("name").getAsString();
        String colorName = event.getOption("color").getAsString();
        Integer color = colorMap.get(colorName.toLowerCase());

        try {
            if (color != null) {
                Role createdRole =(event.getGuild()).createRole()
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

        List<Role> roles = event.getGuild().getRoles();
        String roleList = roles.stream()
                .map(Role::getName)
                .collect(Collectors.joining(", "));

        event.getHook().editOriginal("List of Roles:\n" + roleList).queue();
    }

    private void handleRoleInfoCommand(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        event.deferReply().queue();

        Role role = event.getOption("role").getAsRole();
        if (role != null) {
            String roleInfo = "Role Name: " + role.getName() +
                    "\nRole ID: " + role.getId() +
                    "\nColor: " + role.getColor() +
                    "\nPosition: " + role.getPosition();

            event.getHook().editOriginal(roleInfo).queue();
        } else {
            event.getHook().editOriginal("Role not found or not specified.").queue();
        }
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

}




