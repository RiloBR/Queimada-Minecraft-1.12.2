package br.com.pixelmonbrasil.hotsnowball;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CriarCommand implements CommandExecutor {

    private final HotSnowball plugin;

    public CriarCommand(HotSnowball plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!src.hasPermission("hotsnowball.admin")) {
            src.sendMessage(Text.of(TextColors.RED, "Você não tem permissão para usar este comando."));
            return CommandResult.empty();
        }

        if (plugin.isEventoAtivo()) {
            src.sendMessage(Text.of(TextColors.RED, "O evento já está ativo."));
            return CommandResult.empty();
        }

        plugin.setEventoAtivo(true);
        plugin.getGame().getServer().getBroadcastChannel().send(Text.of(TextColors.GREEN, "O evento Queimada foi criado."));
        return CommandResult.success();
    }
}
