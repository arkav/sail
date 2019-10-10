package dev.arkav.sail.impl;

import dev.arkav.openoryx.game.models.Server;
import dev.arkav.openoryx.impl.Proxy;
import dev.arkav.sail.api.Plugin;
import dev.arkav.sail.api.PluginLoader;

import java.io.IOException;
import java.net.Socket;

public class SailProxy extends Proxy {
    private Plugin[] plugins;
    public SailProxy(Socket clientSocket, Server server, PluginLoader pl) throws IOException {
        super(clientSocket, server);
        this.plugins = pl.instantiatePlugins(this);
    }
}
