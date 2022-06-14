package net.listerily.NinjaAdventure.client;

import net.listerily.NinjaAdventure.communication.PlayerMoveData;
import net.listerily.NinjaAdventure.communication.SCMessage;

public class ClientController {
    public static interface ControllerMessageHandler {
        void submit(SCMessage message);
    }

    private final ControllerMessageHandler controllerMessageHandler;
    public ClientController(ControllerMessageHandler controllerMessageHandler) {
        this.controllerMessageHandler = controllerMessageHandler;
    }

    public void move(boolean down, boolean left, boolean up, boolean right) {
        PlayerMoveData playerMoveData = new PlayerMoveData();
        double dx = 0, dy = 0;
        if (down) dy += 1;
        if (up) dy -= 1;
        if (left) dx -= 1;
        if (right) dx += 1;
        double length = Math.sqrt(dx * dx + dy * dy);
        double scaleTo = 0.1;
        dx /= (length / scaleTo);
        dy /= (length / scaleTo);
        playerMoveData.dx = (float) dx;
        playerMoveData.dy = (float) dy;
        controllerMessageHandler.submit(new SCMessage(SCMessage.MSG_PLAYER_MOVE, playerMoveData));
    }
}
