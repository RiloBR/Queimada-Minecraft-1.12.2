package br.com.pixelmonbrasil.hotsnowball;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Set;

public class EntrarCommand implements CommandExecutor {

    private final Set<String> jogadoresNoEvento;
    private final int maxJogadores;
    private final HotSnowball plugin;

    public EntrarCommand(HotSnowball plugin) {
        this.plugin = plugin;
        this.jogadoresNoEvento = plugin.getJogadoresNoEvento();
        this.maxJogadores = plugin.getMaxJogadores();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("Este comando só pode ser executado por um jogador."));
            return CommandResult.empty();
        }

        if (!plugin.isEventoAtivo()) {
            src.sendMessage(Text.of(TextColors.RED, "O evento não está ativo."));
            return CommandResult.empty();
        }

        Player player = (Player) src;

        if (jogadoresNoEvento.contains(player.getName())) {
            src.sendMessage(Text.of(TextColors.RED, "Você já está no evento."));
            return CommandResult.empty();
        }

        if (jogadoresNoEvento.size() >= maxJogadores) {
            src.sendMessage(Text.of(TextColors.RED, "O evento já está cheio. " + "(" + jogadoresNoEvento + "/" + maxJogadores + ")"));
            return CommandResult.empty();
        }

        jogadoresNoEvento.add(player.getName());
        Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.GREEN, player.getName() + " entrou no evento. " + "(" + jogadoresNoEvento.size() + "/" + maxJogadores + ")"));
        return CommandResult.success();
    }
}
