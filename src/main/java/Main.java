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

        JDA jda = JDABuilder.createDefault("MTE3OTE0NzgwNjc5MTk2MjY4NA.GFygU1.XT9uhdb4ev9ZnU6b5R22QjXCAUk8hV9vz3kYPo")
                .setActivity(Activity.playing("being a bot"))
                .addEventListeners(new Greeting(), new onJoin(),
                        new CommandHandler())
                .build()
                .awaitReady();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                Commands.slash("info", "Displays information about the bot"),
                Commands.slash("server", "Displays server information"),
                Commands.slash("members", "Displays member count"),
                Commands.slash("memberlist", "Displays a full list of members"),

                Commands.slash("createrole", "Creates a new role")
                        .addOption(OptionType.STRING, "name","Enter the name of the role:")
                        .addOption(OptionType.STRING, "color", "Enter the role color (e.g., red, blue, black)")
        ).queue();
    }
}
