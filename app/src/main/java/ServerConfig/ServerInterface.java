package ServerConfig;

import java.util.HashMap;

public interface ServerInterface {
    public String ping();

    public String register(String uname);

    public boolean checkActive();

    public HashMap<String, String> newGameData();

    public void answer(String uname, String answer);
}
