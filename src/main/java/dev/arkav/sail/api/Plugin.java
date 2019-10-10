package dev.arkav.sail.api;

import dev.arkav.sail.impl.SailProxy;

public interface Plugin {
    void initalize(SailProxy proxy);
}
