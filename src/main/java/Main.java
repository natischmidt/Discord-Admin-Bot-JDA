import events.MyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import javax.security.auth.login.LoginException;


public class Main
{
    public static void main(String[] args) throws LoginException {

        JDA bot = JDABuilder.createDefault

                ("MTE3OTE0NzgwNjc5MTk2MjY4NA.GFygU1.XT9uhdb4ev9ZnU6b5R22QjXCAUk8hV9vz3kYPo")
                .addEventListeners(new MyListener())
                .build();

    }

}
