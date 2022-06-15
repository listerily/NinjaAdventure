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
        if (up && down && left && right)
            return;
        if (!up && !down && !left && !right)
            return;
        if (up && down && !left && !right)
            return;
        if (!up && !down && left && right)
            return;
        PlayerMoveData playerMoveData = new PlayerMoveData();
        double dx = 0, dy = 0;
        if (down) dy += 1.0;
        if (up) dy -= 1.0;
        if (left) dx -= 1.0;
        if (right) dx += 1.0;
        double length = Math.sqrt(dx * dx + dy * dy);
        double scaleTo = 0.14;
        dx /= (length / scaleTo);
        dy /= (length / scaleTo);
        playerMoveData.dx = (float) dx;
        playerMoveData.dy = (float) dy;
        controllerMessageHandler.submit(new SCMessage(SCMessage.MSG_PLAYER_MOVE, playerMoveData));
    }

    public void attack() {
        controllerMessageHandler.submit(new SCMessage(SCMessage.MSG_PLAYER_ATTACK));
    }
}
