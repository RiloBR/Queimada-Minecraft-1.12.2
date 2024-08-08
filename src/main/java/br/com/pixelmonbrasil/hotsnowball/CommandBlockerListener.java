package br.com.pixelmonbrasil.hotsnowball;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CommandBlockerListener {

    private final HotSnowball plugin;
    private final Set<String> allowedCommands = new HashSet<>(Arrays.asList("queimada", "help"));

    public CommandBlockerListener(HotSnowball plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onCommand(SendCommandEvent event) {
        if (event.getCause().root() instanceof Player) {
            Player player = (Player) event.getCause().root();

            if (plugin.getJogadoresNoEvento().contains(player.getName())) {
                String command = event.getCommand();

                if (!allowedCommands.contains(command.toLowerCase())) {
                    player.sendMessage(Text.of(TextColors.RED, "Você não pode usar esse comando durante o evento."));
                    event.setCancelled(true);
                }
            }
        }
    }
}
