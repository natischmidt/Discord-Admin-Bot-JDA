package events;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class RoleCreationListener extends ListenerAdapter {

    private String roleName;
    private ButtonInteractionEvent  originalEvent;
    private boolean promptAgain = false;
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

    public RoleCreationListener(User user, ButtonInteractionEvent event) {
        this.originalEvent = event;
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().equals(originalEvent.getUser())) {
            String content = event.getMessage().getContentRaw();

            if (roleName == null) {
                roleName = content;
                originalEvent.getChannel().sendMessage("Enter the role color (e.g., red, blue, black):").queue();
            } else {
                try {
                    Integer color = colorMap.get(content.toLowerCase());

                    if (color != null) {
                        originalEvent.getGuild().createRole()
                                .setName(roleName)
                                .setColor(color)
                                .setHoisted(true)
                                .queue(createdRole -> originalEvent.getChannel().sendMessage("New role created: "
                                        + createdRole.getName()).queue());
                    } else {
                        String validColorNames = String.join(", ", colorMap.keySet());
                        originalEvent.getChannel().sendMessage("Invalid color name." +
                                " Please provide a valid color name. Valid names: " + validColorNames).queue();
                        promptAgain = true;
                    }

                    if (!promptAgain) {
                        event.getJDA().removeEventListener(this);
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}

