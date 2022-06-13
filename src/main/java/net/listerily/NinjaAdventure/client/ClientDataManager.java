package net.listerily.NinjaAdventure.client;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.communication.SCMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientDataManager {
    private App app;
    public ClientDataManager(App app) {
        this.app = app;
    }

    public void initialize(ObjectInputStream inputStream, ObjectOutputStream outputStream) {

    }

    public void tick()  {

    }
}
