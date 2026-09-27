package net.clydo.clytil.iface;

public interface CloseableScope extends AutoCloseable {

    @Override
    void close();

}