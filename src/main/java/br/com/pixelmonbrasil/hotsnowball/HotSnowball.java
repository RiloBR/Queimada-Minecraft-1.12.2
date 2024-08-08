package br.com.pixelmonbrasil.hotsnowball;

import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.text.Text;
import com.google.inject.Inject;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Plugin(
        id = "hotsnowball",
        name = "HotSnowball",
        version = "1.0",
        description = "Evento Queimada",
        authors = {
                "RiloBR"
        }
)
public class HotSnowball {

        @Inject
        private Game game;

        @Inject
        private Logger logger;

        private final Set<String> jogadoresNoEvento = new HashSet<>();
        private final int maxJogadores = 16;
        private boolean eventoAtivo = false;

        private final EventoManager eventoManager = new EventoManager(this);

        @Listener
        public void onInit(GameInitializationEvent event) {
                CommandSpec entrarCmd = CommandSpec.builder()
                        .description(Text.of("Entrar no evento"))
                        .executor(new EntrarCommand(this))
                        .build();

                CommandSpec sairCmd = CommandSpec.builder()
                        .description(Text.of("Sair do evento"))
                        .executor(new SairCommand(this))
                        .build();

                CommandSpec criarCmd = CommandSpec.builder()
                        .description(Text.of("Criar o evento"))
                        .executor(new CriarCommand(this))
                        .build();

                CommandSpec iniciarCmd = CommandSpec.builder()
                        .description(Text.of("Iniciar o evento"))
                        .executor(new IniciarCommand(this))
                        .build();

                CommandSpec finalizarCmd = CommandSpec.builder()
                        .description(Text.of("Finalizar o evento"))
                        .executor(new FinalizarCommand(this))
                        .build();


                CommandSpec queimadaCmd = CommandSpec.builder()
                        .description(Text.of("Comando do evento que possui subcomandos"))
                        .child(criarCmd, "criar")
                        .child(entrarCmd, "entrar")
                        .child(sairCmd, "sair")
                        .child(iniciarCmd, "iniciar")
                        .child(finalizarCmd, "finalizar")
                        .build();

                game.getCommandManager().register(this, queimadaCmd, "queimada");

                Sponge.getEventManager().registerListeners(this, new CommandBlockerListener(this));

                Sponge.getEventManager().registerListeners(this, new SnowballHitListener(this));
        }


        public boolean isEventoAtivo() {
                return eventoAtivo;
        }

        public void setEventoAtivo(boolean eventoAtivo) {
                this.eventoAtivo = eventoAtivo;
        }

        public Set<String> getJogadoresNoEvento() {
                return jogadoresNoEvento;
        }

        public int getMaxJogadores() {
                return maxJogadores;
        }

        public EventoManager getEventoManager() {
                return eventoManager;
        }

        public Game getGame() {
                return game;
        }
}
