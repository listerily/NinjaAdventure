package net.listerily.NinjaAdventure.server.data.entities;

import net.listerily.NinjaAdventure.communication.MonsterData;
import net.listerily.NinjaAdventure.communication.SCMessage;
import net.listerily.NinjaAdventure.server.TickMessageHandler;
import net.listerily.NinjaAdventure.server.data.World;

import java.util.Random;
import java.util.UUID;

public class Monster extends Entity {
    protected int type;
    public Monster(World world, UUID uuid) {
        super(world, uuid);
        this.type = new Random().nextInt(10);
    }

    protected int monsterMoving = 0;
    protected float moveStepX, moveStepY;
    @Override
    public void tick(TickMessageHandler handler) {
        super.tick(handler);

        if (isDead())
            return;

        Random random = new Random();
        if (monsterMoving > 0) {
            --monsterMoving;
            walk(moveStepX, moveStepY);
            markUpdated();
        } else if (monsterMoving == 0 && random.nextInt(40) == 0) {
            monsterMoving = 10;
            moveStepX = (random.nextInt(10) - 5) * 0.014f;
            moveStepY = (random.nextInt(10) - 5) * 0.014f;
        }

        for (Player player : world.getPlayers()) {
            if (player.getScene() == getScene()) {
                if (player.getPosition().distance(getPosition()) < 0.5f && !player.isHurting() && !player.dead) {
                    player.hurt(1, this);
                    player.markUpdated();
                }
            }
        }
    }

    public int getType() {
        return type;
    }

    @Override
    public int getMaxHealth() {
        return 4;
    }

    @Override
    public void yieldUpdateMessage(TickMessageHandler handler) {
        super.yieldUpdateMessage(handler);
        handler.submit(new SCMessage(SCMessage.MSG_MONSTER_UPDATE, MonsterData.generateMonsterData(this).clone()));
    }
}
