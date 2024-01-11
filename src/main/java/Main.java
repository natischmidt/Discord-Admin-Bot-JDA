import events.CommandHandler;
import events.Greeting;
import events.onJoin;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;


public class Main
{
    public static void main(String[] args) throws LoginException {

        JDABuilder.createDefault("MTE3OTE0NzgwNjc5MTk2MjY4NA.GFygU1.XT9uhdb4ev9ZnU6b5R22QjXCAUk8hV9vz3kYPo")
                .setActivity(Activity.playing("being a bot"))
                .addEventListeners(new Greeting(), new onJoin(),new CommandHandler())
                .build();

    }

}
