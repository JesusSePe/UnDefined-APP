package ServerConfig;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import lipermi.exception.LipeRMIException;
import lipermi.handler.CallHandler;
import lipermi.net.IServerListener;

public class Server implements ServerInterface {
    // properties
    ArrayList<String> users = new ArrayList<String>();
    boolean isActive = false;
    HashMap<String, String> gameData = null;
    String correct;
    HashMap<String, Integer> ranking = null;
    long time_started;

    // Setters and Getters

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public HashMap<String, String> getGameData() {
        return gameData;
    }

    public void setGameData(HashMap<String, String> gameData) {
        /*
         * Structure should be like this:
         * pregunta = pregunta
         * resposta1 = resposta1
         * resposta2 = resposta2
         * ...
         * temps_preguntes = Y segons
         * temps_inici = X segons
         */
        this.gameData = gameData;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public HashMap<String, Integer> getRanking() {
        return ranking;
    }

    public void setRanking(HashMap<String, Integer> ranking) {
        this.ranking = ranking;
    }

    public long getTime_started() {
        return time_started;
    }

    public void setTime_started() {
        this.time_started = System.currentTimeMillis();
    }


    // Constructor
    public Server() {
        try {
            CallHandler callHandler = new CallHandler();
            callHandler.registerGlobal(ServerInterface.class, this);
            lipermi.net.Server server = new lipermi.net.Server();
            server.bind(7777, callHandler);
            server.addServerListener(new IServerListener() {

                @Override
                public void clientDisconnected(Socket socket) {
                    System.out.println("Client Disconnected: " + socket.getInetAddress());
                }

                @Override
                public void clientConnected(Socket socket) {
                    System.out.println("Client Connected: " + socket.getInetAddress());
                }
            });
            System.out.println("Server Listening");
        } catch (LipeRMIException | IOException e) {
            e.printStackTrace();
        }
    }

    // ping function
    @Override
    public String ping() {
        System.out.println("A client is pinging!");
        return "ping";
    }

    // Register function
    @Override
    public String register(String uname) {
        System.out.println(uname + "Is trying to connect!");
        String valid = "true";
        String username = uname;

        for (String aname : this.getUsers()) {
            if (aname.equals(username)) {
                valid = "false";
            }
        }

        if (valid.equals("true")) {
            users.add(username);
            valid = "true";
            System.out.println("User added.");
        } else {
            valid = "KAD-0001";
            System.out.println("Acces denied.");
        }

        return valid;

    }

    @Override
    public boolean checkActive() {
        System.out.println(getIsActive());
        return getIsActive();
    }

    @Override
    public HashMap<String, String> newGameData() {
        return getGameData();
    }

    @Override
    public void answer(String uname, String answer) {
        // Check if answer is correct
        if (answer.equals(getCorrect())) {
            // Calculate question points
            long curTime = System.currentTimeMillis();
            long startTime = getTime_started();
            long difMilliseconds = curTime-startTime;;
            Integer secs = Integer.parseInt(getGameData().get("temps_preguntes"));
            Integer questionPts = Math.round(1000-(difMilliseconds/secs)+500);

            // Add the new points to user's current points.
            Integer oldPts = (Integer) getRanking().get(uname);
            if (oldPts.equals(null)) {
                oldPts = 0;
            }
            Integer usrPts = oldPts + questionPts;

            // Update user points
            getRanking().put(uname, usrPts);
        }
    }

}
