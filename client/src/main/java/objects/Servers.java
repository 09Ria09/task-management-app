package objects;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Servers implements Serializable {
    private static final Servers serversSingleton = new Servers();
    Map<String, List<Long>> map;

    public Servers() {
        this.map = new HashMap<>();
    }

    public static Servers getInstance() {
        return serversSingleton;
    }

    public Map<String, List<Long>> getServers() {
        return map;
    }

    public void setServers(final Map<String, List<Long>> map) {
        this.map = map;
    }

    public void save() {
        try (ObjectOutputStream objectWriter = new ObjectOutputStream(
            new FileOutputStream("servers"))) {
            objectWriter.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        try (ObjectInputStream objectWriter = new ObjectInputStream(
            new FileInputStream("servers"))) {
            Servers servers = (Servers) objectWriter.readObject();
            this.setServers(servers.getServers());
        } catch (IOException e) {
            System.out.println("servers file not found!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
