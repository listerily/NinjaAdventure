package net.listerily.NinjaAdventure.server;

import net.listerily.NinjaAdventure.communication.SCMessage;

public interface TickMessageHandler {
    void handleMessage(SCMessage message);
}