package me.metropanties.notepad.config.discord;

import lombok.RequiredArgsConstructor;
import me.metropanties.springdiscordstarter.command.CommandManager;
import me.metropanties.springdiscordstarter.discord.annotation.EnableDiscord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.awt.*;
import java.time.Instant;

@Configuration
@EnableDiscord
@RequiredArgsConstructor
public class JDAConfig {

    private final JDA jda;
    private final ApplicationContext context;

    @Bean
    public CommandManager commandManager() {
        return new CommandManager(this.jda, context);
    }

    @Bean
    @Scope("prototype")
    public EmbedBuilder embedBuilder() {
        return new EmbedBuilder()
                .setColor(Color.decode("#FD7E14"))
                .setTimestamp(Instant.now())
                .setFooter(this.jda.getSelfUser().getName(), this.jda.getSelfUser().getAvatarUrl());
    }

}
