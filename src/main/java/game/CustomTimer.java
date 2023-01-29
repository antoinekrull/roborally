package game;

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
                    logger.debug("heißt das der kacker macht das hier alles?");
                    PlayerList unreadyPlayers = game.getPlayerList().getUnreadyPlayers();
                    for (int i = 0; i < unreadyPlayers.size(); i++) {
                        logger.debug("ja das heißt es");
                        String[] placedCards = unreadyPlayers.get(i).fillRegisterWithRandomCards();
                        server.sendCardsYouGotNow(unreadyPlayers.get(i), placedCards);
                        unreadyPlayers.get(i).fillRegisterWithRandomCards();
                        //for testing purposes
                        unreadyPlayers.get(i).printRegisters();
                    }
                    game.getPlayerList().setPlayerReadiness(true);
                    server.sendTimerEnded(new PlayerList());
                   game.setTimerIsRunning(false);
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
