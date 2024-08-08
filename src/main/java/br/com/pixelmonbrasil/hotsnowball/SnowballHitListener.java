package br.com.pixelmonbrasil.hotsnowball;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.Snowball;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SnowballHitListener {

    private final HotSnowball plugin;

    public SnowballHitListener(HotSnowball plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onSnowballHit(CollideEntityEvent.Impact event, @First Snowball snowball) {
        if (snowball.getShooter() instanceof Player) {
            Player shooter = (Player) snowball.getShooter();
            String shooterName = shooter.getName();

            for (Entity hitEntity : event.getEntities()) {
                if (hitEntity instanceof Player) {
                    Player hitPlayer = (Player) hitEntity;
                    String hitPlayerName = hitPlayer.getName();

                    if (plugin.getEventoManager().getTimeAzul().contains(shooterName)) {
                        if (plugin.getEventoManager().getTimeVermelho().contains(hitPlayerName)) {
                            hitPlayer.offer(Keys.HEALTH, 0.0);
                            Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.RED, hitPlayerName + " foi eliminado pelo time azul!"));
                            plugin.getEventoManager().removePlayerFromEvent(hitPlayerName);
                        }
                    } else if (plugin.getEventoManager().getTimeVermelho().contains(shooterName)) {
                        if (plugin.getEventoManager().getTimeAzul().contains(hitPlayerName)) {
                            hitPlayer.offer(Keys.HEALTH, 0.0);
                            Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.RED, hitPlayerName + " foi eliminado pelo time vermelho!"));
                            plugin.getEventoManager().removePlayerFromEvent(hitPlayerName);
                        }
                    }
                }
            }
        }
    }

    @Listener
    public void onEntityImpact(CollideEntityEvent.Impact event) {
        if (event.getCause().root() instanceof Snowball) {
            Snowball snowball = (Snowball) event.getCause().root();
            for (Entity entity : event.getEntities()) {
                System.out.println("Snowball hit entity: " + entity.getType().getName());
            }
        }
    }
}

