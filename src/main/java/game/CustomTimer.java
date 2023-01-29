package game;

import game.player.AI_Player;
import game.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.connection.PlayerList;
import server.connection.Server;

import java.util.Timer;
import java.util.TimerTask;

//custom timer class to be newly initialised for each programming phase
public class CustomTimer {
    private Timer timer;
    private Game game;
    private Server server;

    private final Logger logger = LogManager.getLogger(CustomTimer.class);

    //constructor
    public CustomTimer(Server server) {
        timer = new Timer();
        game = Game.getInstance();
        this.server = server;
    }

    //runs necessary timer logic
    public void runTimer() {
        try{
            game.setTimerIsRunning(true);
            server.sendTimerStarted();
            System.out.println("Timer is running");
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    logger.info("Time ran through");
                    PlayerList unreadyPlayers = game.getPlayerList().getUnreadyPlayers();
                    for (Player unreadyPlayer : unreadyPlayers.getPlayerList()) {
                        String[] placedCards = unreadyPlayer.fillRegisterWithRandomCards();
                        if(!(unreadyPlayer instanceof AI_Player)) {
                            server.sendCardsYouGotNow(unreadyPlayer, placedCards);
                        }
                        unreadyPlayer.fillRegisterWithRandomCards();
                        game.getPlayerList().setPlayerReadiness(true);
                        game.setTimerIsRunning(false);
                    }
                    game.getPlayerList().setPlayerReadiness(true);
                    server.sendTimerEnded(new PlayerList());
                }
            };
            timer.schedule(timerTask, 30000);
        } catch (Exception e) {
            logger.warn("Timer error go tell Molri");
        }
    }

    //cancels timer
    public void cancel() {
        timer.cancel();
        server.sendTimerEnded(new PlayerList());
        logger.info("Timer cancelled");
    }

}
