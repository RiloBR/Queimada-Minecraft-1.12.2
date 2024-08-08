package br.com.pixelmonbrasil.hotsnowball;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class IniciarCommand implements CommandExecutor {

    private final HotSnowball plugin;

    public IniciarCommand(HotSnowball plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!src.hasPermission("hotsnowball.admin")) {
            src.sendMessage(Text.of(TextColors.RED, "Você não tem permissão para usar este comando."));
            return CommandResult.empty();
        }

            if (!plugin.isEventoAtivo()) {
                src.sendMessage(Text.of(TextColors.RED, "O evento não está ativo. Use /queimada criar para iniciar o evento."));
                return CommandResult.empty();
            }


            plugin.getEventoManager().iniciarContagemParaEvento(10);

            src.sendMessage(Text.of(TextColors.GREEN, "O evento irá se iniciar em 10 segundos."));
            return CommandResult.success();
        }
    }
