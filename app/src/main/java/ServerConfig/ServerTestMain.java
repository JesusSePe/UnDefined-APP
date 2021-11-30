package ServerConfig;

import java.util.HashMap;

public class ServerTestMain {

    public static void main(String[] args) {
        Server server = new Server();
        server.setIsActive(true);
        HashMap<String, String> info = new HashMap();
        info.put("pregunta", "Preg1");
        info.put("resposta1", "1");
        info.put("resposta2", "2");
        info.put("resposta3", "3");
        info.put("resposta4", "Demasiados");
        info.put("temps_preguntes", "15");
        info.put("temps_inici", "5");
        server.setGameData(info);
    }

}
