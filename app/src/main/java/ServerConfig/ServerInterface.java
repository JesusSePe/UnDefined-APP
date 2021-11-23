package ServerConfig;

public interface ServerInterface {
    public String ping();

    public String register(String uname);

    public boolean checkActive();
}
