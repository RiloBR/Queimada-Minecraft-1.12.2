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
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Set;

public class SairCommand implements CommandExecutor {

    private final Set<String> jogadoresNoEvento;
    private final int maxJogadores;
    private final HotSnowball plugin;

    public SairCommand(HotSnowball plugin) {
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

        if (!jogadoresNoEvento.contains(player.getName())) {
            src.sendMessage(Text.of(TextColors.RED, "Você não está no evento."));
            return CommandResult.empty();
        }

        jogadoresNoEvento.remove(player.getName());
        plugin.getEventoManager().getTimeAzul().remove(player.getName());
        plugin.getEventoManager().getTimeVermelho().remove(player.getName());

        World world = player.getWorld();
        Location<World> spawnLocation = world.getSpawnLocation();
        player.setLocation(spawnLocation);

        player.sendMessage(Text.of(TextColors.GREEN, "Você saiu do evento e foi teletransportado para o spawn."));
        Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.RED, player.getName() + " saiu do evento. " + "(" + jogadoresNoEvento.size() + "/" + maxJogadores + ")"));

        plugin.getEventoManager().checkWinner();

        return CommandResult.success();
    }
}
