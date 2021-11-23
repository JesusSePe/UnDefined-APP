package ServerConfig;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import lipermi.exception.LipeRMIException;
import lipermi.handler.CallHandler;
import lipermi.net.IServerListener;

public class Server implements ServerInterface {
    // properties
    ArrayList<String> users = new ArrayList<String>();
    boolean isActive = false;

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
        return getIsActive();
    }

}
