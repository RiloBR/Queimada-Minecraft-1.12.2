package br.com.pixelmonbrasil.hotsnowball;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class EventoManager {

    private final HotSnowball plugin;
    private final Set<String> timeAzul = new HashSet<>();
    private final Set<String> timeVermelho = new HashSet<>();
    private final Set<String> jogadoresNoEvento;

    public EventoManager(HotSnowball plugin) {
        this.plugin = plugin;
        this.jogadoresNoEvento = plugin.getJogadoresNoEvento();
    }

    public Set<String> getTimeAzul() {
        return timeAzul;
    }

    public Set<String> getTimeVermelho() {
        return timeVermelho;
    }

    public Set<String> getJogadoresNoEvento() {
        return jogadoresNoEvento;
    }

    public void iniciarContagemParaEvento(int segundos) {
        Task.builder()
                .execute(this::distribuirJogadoresEmTimes)
                .delay(segundos, TimeUnit.SECONDS)
                .submit(plugin);
    }

    private void distribuirJogadoresEmTimes() {
        Set<String> jogadores = plugin.getJogadoresNoEvento();
        if (jogadores.size() < 1) {
            Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.RED, "Não há jogadores suficientes para iniciar o evento."));
            plugin.setEventoAtivo(false);
            return;
        }

        List<String> jogadoresList = new ArrayList<>(jogadores);
        Collections.shuffle(jogadoresList);

        for (int i = 0; i < jogadoresList.size(); i++) {
            if (i % 2 == 0) {
                timeAzul.add(jogadoresList.get(i));
            } else {
                timeVermelho.add(jogadoresList.get(i));
            }
        }

        teleportarJogadoresParaArena();
        iniciarEvento();
    }

    private void teleportarJogadoresParaArena() {
        World world = Sponge.getServer().getWorld("world").get(); // Substitua o "world" pelo mundo/dimensão
        Location<World> spawnAzul = new Location<>(world, 109, 21, -46);
        Location<World> spawnVermelho = new Location<>(world, 109, 21, -10);

        for (String playerName : timeAzul) {
            Player player = Sponge.getServer().getPlayer(playerName).orElse(null);
            if (player != null) {
                player.setLocation(spawnAzul);
                player.sendMessage(Text.of(TextColors.BLUE, "Você está no time azul!"));
            }
        }

        for (String playerName : timeVermelho) {
            Player player = Sponge.getServer().getPlayer(playerName).orElse(null);
            if (player != null) {
                player.setLocation(spawnVermelho);
                player.sendMessage(Text.of(TextColors.RED, "Você está no time vermelho!"));
            }
        }
    }

    private void iniciarEvento() {
        plugin.setEventoAtivo(true);
        Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.GREEN, "O evento Queimada começou!"));

        Task.builder()
                .execute(this::distribuirBolasDeNeve)
                .interval(7, TimeUnit.SECONDS)
                .submit(plugin);
    }

    private void distribuirBolasDeNeve() {
        for (String playerName : timeAzul) {
            Player player = Sponge.getServer().getPlayer(playerName).orElse(null);
            if (player != null) {
                player.getInventory().offer(ItemStack.of(ItemTypes.SNOWBALL, 1));
            }
        }

        for (String playerName : timeVermelho) {
            Player player = Sponge.getServer().getPlayer(playerName).orElse(null);
            if (player != null) {
                player.getInventory().offer(ItemStack.of(ItemTypes.SNOWBALL, 1));
            }
        }
    }

    public void teleportarJogadorParaSpawn(Player player) {
        World world = Sponge.getServer().getWorld("world").get();
        Location<World> spawnLocation = world.getSpawnLocation();
        player.setLocation(spawnLocation);
        player.sendMessage(Text.of(TextColors.RED, "Você foi eliminado do evento e teletransportado para o spawn!"));
    }

    public void removePlayerFromEvent(String playerName) {
        jogadoresNoEvento.remove(playerName);
        timeAzul.remove(playerName);
        timeVermelho.remove(playerName);

        Player player = Sponge.getServer().getPlayer(playerName).orElse(null);
        if (player != null) {
            teleportarJogadorParaSpawn(player);
        }

        checkWinner();
    }

    public void checkWinner() {
        if (timeAzul.isEmpty()) {
            declareWinner("Vermelho");
        } else if (timeVermelho.isEmpty()) {
            declareWinner("Azul");
        } else if (timeAzul.size() + timeVermelho.size() == 1) {

            if (!timeAzul.isEmpty()) {
                declareWinner("Azul");
            } else if (!timeVermelho.isEmpty()) {
                declareWinner("Vermelho");
            }
        }
    }

    private void declareWinner(String winningTeam) {
        Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.GOLD, "O time " + winningTeam + " venceu o evento!"));
        endEvent();
    }

    public void endEvent() {
        timeAzul.clear();
        timeVermelho.clear();
        jogadoresNoEvento.clear();

        plugin.setEventoAtivo(false);
        Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.RED, "O evento Queimada foi encerrado!"));
    }
}
