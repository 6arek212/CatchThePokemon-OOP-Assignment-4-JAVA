package GameClient;

import GameClient.utils.Point;
import GameGui.GameFrame;
import GameGui.LoginFrame;
import api.*;
import implementation.AlgorithmsImpl;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;


/**
 * This class represent the main brain of the game
 */


public class GameRunner implements Runnable {
    private static DirectedWeightedGraph graph;
    private static GameWorld gameWorld;
    private static GameFrame gameFrame;
    private static int agentSize;
    public static Client game;
    private long id;



    public GameRunner(long id) {
        this.id = id;
    }


    /**
     * Invoking agentController and run the game
     */
    @Override
    public void run() {
        game = new Client();
        try {
            game.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameWorld = new GameWorld();
        graph = gameWorld.fromJsonToGraphGame(game.getGraph());
        DirectedWeightedGraphAlgorithms ga = new AlgorithmsImpl();
        ga.init(graph);
        game.login(Long.toString(id));
        initGame(game);
        AgentController agentController = new AgentController(graph, gameWorld);
//        game.addAgent("{\"id\":0}");
        game.start();
        while (game.isRunning().equals("true")) {
            agentController.moveAgents(game);
            gameFrame.repaint();
            gameWorld.setTimeToend((Integer.parseInt(GameRunner.game.timeToEnd()) / 1000));
            gameWorld.setInfo(game.getInfo());
            try {
                Thread.sleep((long) AgentController.ms);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        System.exit(0);
    }

    /**
     * Initialize the game , get Graph , get Agents , get Pokemons
     */
    private void initGame(Client game) {
        String pokz = game.getPokemons();
        gameWorld.setGraph(graph);
        gameWorld.setPokemons(GameWorld.fromJsonStringToPoks(pokz));
        String info = game.getInfo();
        gameFrame = new GameFrame(gameWorld);
        gameFrame.repaint();
        try {
            JSONObject infoObject = new JSONObject(info);
            JSONObject GameServer = infoObject.getJSONObject("GameServer");
            agentSize = GameServer.getInt("agents");
            ArrayList<Pokemon> p = GameWorld.fromJsonStringToPoks(game.getPokemons());
            p.sort(Comparator.comparingInt(o -> (int) o.getValue()));
            gameWorld.updatePokemonsEdges(p);
            //System.out.println(agentSize);
            Random random = new Random();
            for (int i = 0; i < agentSize; i++) {
                game.addAgent("{\"id\":" + i + "}");
            }
            gameWorld.setAgents(GameWorld.getAgents(game.getAgents(), graph));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
