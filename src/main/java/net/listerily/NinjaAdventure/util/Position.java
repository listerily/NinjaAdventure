package net.listerily.NinjaAdventure.util;

import java.io.Serializable;

public class Position implements Serializable, Cloneable {
    public float x;
    public float y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
        this(0.f, 0.f);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void move(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Position position) {
        this.x = position.x;
        this.y = position.y;
    }

    @Override
    public Position clone() {
        try {
            Position clone = (Position) super.clone();
            clone.x = x;
            clone.y = y;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
