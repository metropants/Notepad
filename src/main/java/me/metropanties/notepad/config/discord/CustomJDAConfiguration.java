package me.metropanties.notepad.config.discord;

import me.metropanties.springdiscordstarter.discord.JDAConfiguration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Primary
public class CustomJDAConfiguration implements JDAConfiguration {

    @Value("${discord.bot.token}")
    private String token;

    @Override
    public JDA jda(@NotNull String token) {
        return JDABuilder.createDefault(this.token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();
    }

}
