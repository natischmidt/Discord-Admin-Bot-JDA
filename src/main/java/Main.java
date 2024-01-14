import events.AutoCompleteBot;
import events.CommandHandler;
import events.Greeting;
import events.onJoin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException, InterruptedException {

        JDA jda = JDABuilder.createDefault
                 ("MTE3OTE0NzgwNjc5MTk2MjY4NA.GFygU1.XT9uhdb4ev9ZnU6b5R22QjXCAUk8hV9vz3kYPo")
                .setActivity(Activity.playing("being a bot"))
                .addEventListeners(new Greeting(), new onJoin(), new CommandHandler(),new AutoCompleteBot())
                .build()
                .awaitReady();

        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
                Commands.slash("info", "Displays information about the bot"),
                Commands.slash("server", "Displays server information"),
                Commands.slash("members", "Displays member count"),
                Commands.slash("nickname", "Set or modify the nickname of a member")
                        .addOption(OptionType.USER, "user", "Member to set the nickname for", true)
                        .addOption(OptionType.STRING, "nickname", "The desired nickname", true),
                Commands.slash("member-list", "Displays a full list of members"),
                Commands.slash("help", "Displays all commands"),
                Commands.slash("ban", "Ban a member")
                .addOption(OptionType.USER, "user", "Member to ban", true),
                Commands.slash("kick", "Kick a member")
                .addOption(OptionType.USER, "user", "Member to kick", true),
                Commands.slash("create-role", "Creates a new role")
                .addOption(OptionType.STRING, "name","Enter the name of the role",
                 true,false)
                .addOption(OptionType.STRING, "color", "Enter the role color",
                 true,true),
                Commands.slash("list-roles", "Displays a list of all roles in the server"),
                Commands.slash("role-info", "Displays information about a specific role")
                        .addOption(OptionType.ROLE, "role", "Role to get information about", true)
        ).queue();
    }
}
