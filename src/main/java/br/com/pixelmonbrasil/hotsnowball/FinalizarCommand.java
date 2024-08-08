package br.com.pixelmonbrasil.hotsnowball;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Set;

public class FinalizarCommand implements CommandExecutor {

    private final HotSnowball plugin;

    public FinalizarCommand(HotSnowball plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!src.hasPermission("hotsnowball.admin")) {
            src.sendMessage(Text.of(TextColors.RED, "Você não tem permissão para usar este comando."));
            return CommandResult.empty();
        }

        if (!plugin.isEventoAtivo()) {
            src.sendMessage(Text.of(TextColors.RED, "Não há evento ativo para finalizar."));
            return CommandResult.empty();
        }

        plugin.getJogadoresNoEvento().clear();


        plugin.setEventoAtivo(false);


        plugin.getEventoManager().getTimeAzul().clear();
        plugin.getEventoManager().getTimeVermelho().clear();
        
        src.sendMessage(Text.of(TextColors.GREEN, "O evento foi finalizado."));
        plugin.getGame().getServer().getBroadcastChannel().send(Text.of(TextColors.RED, "O evento Queimada foi finalizado."));

        return CommandResult.success();
    }
}
