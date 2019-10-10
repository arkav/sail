package dev.arkav.sail;

import dev.arkav.openoryx.game.appspot.ServerMapper;
import dev.arkav.openoryx.net.PacketMapper;
import dev.arkav.openoryx.util.logging.Logger;
import dev.arkav.sail.api.PluginLoader;
import dev.arkav.sail.impl.SailProxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sail {
    public void start() {
        try {
            PacketMapper.mapIds(Paths.get("packets.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerMapper.mapServers(true);
        PluginLoader pl = new PluginLoader();
        pl.loadPlugins("plugins");
        try {
            ServerSocket listener = new ServerSocket(2050);
            Logger.log("Sail", "Proxy started on port 2050");
            ExecutorService pool = Executors.newFixedThreadPool(50);
            while (true) {
                pool.execute(new SailProxy(listener.accept(), ServerMapper.get("USSouthWest"), pl));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
