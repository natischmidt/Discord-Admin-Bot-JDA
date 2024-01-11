package events;


import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;




public class onJoin extends ListenerAdapter{

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        try {

            String user = event.getMember().getEffectiveName();

            event
                    .getGuild()
                    .getDefaultChannel()
                    .sendMessage("Welcome '" + user + "' to '" + event.getGuild().getName() + "'")
                    .queue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
