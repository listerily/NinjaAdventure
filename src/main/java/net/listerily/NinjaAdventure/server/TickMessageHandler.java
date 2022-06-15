package net.listerily.NinjaAdventure.server;

import net.listerily.NinjaAdventure.communication.SCMessage;

import java.util.UUID;

public interface TickMessageHandler {
    void submit(SCMessage message);
    void submitTo(SCMessage message, UUID clientUUID);
}
