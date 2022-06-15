package net.listerily.NinjaAdventure.server.data.entities;

import net.listerily.NinjaAdventure.communication.PlayerData;
import net.listerily.NinjaAdventure.communication.SCMessage;
import net.listerily.NinjaAdventure.server.TickMessageHandler;
import net.listerily.NinjaAdventure.server.data.World;
import net.listerily.NinjaAdventure.util.Position;

import java.util.UUID;

public class Player extends Entity {
    protected String nickname;
    protected String character;
    public Player(World world, UUID playerUUID) {
        super(world, playerUUID);
        this.setHealth(getMaxHealth());
        this.nickname = "UNKNOWN";
        this.character = "BlueNinja";
    }

    @Override
    public int getMaxHealth() {
        return 12;
    }

    public void moveToSpawn() {
        setPosition(getScene().getSpawnPosition());
    }

    public String getNickname() {
        return nickname;
    }

    public String getCharacter() {
        return character;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void attack() {
        setAttackingAction();
        Position rangeCenter = new Position();
        switch (getFacing())
        {
            case FACING_UP:
                rangeCenter.set(getPosition().x, getPosition().y - 0.3f);
            case FACING_DOWN:
                rangeCenter.set(getPosition().x, getPosition().y + 0.3f);
            case FACING_LEFT:
                rangeCenter.set(getPosition().x - 0.3f, getPosition().y);
            case FACING_RIGHT:
                rangeCenter.set(getPosition().x + 0.3f, getPosition().y);
        }
        for (Monster monster : getScene().getMonsters()) {
            if (!monster.isDead() && monster.getPosition().distance(rangeCenter) < 1.3f) {
                monster.hurt(1, this);
                monster.markUpdated();
            }
        }
    }

    @Override
    public void yieldUpdateMessage(TickMessageHandler handler) {
        super.yieldUpdateMessage(handler);
        handler.submit(new SCMessage(SCMessage.MSG_UPDATE_PLAYER_DATA, PlayerData.generatePlayerData(this).clone()));
    }
}
