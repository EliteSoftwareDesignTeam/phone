package com.teamness.smane.containers;

/**
 * Created by mac15001900 on 3/1/18.
 */

public class Command {

    public CommandType type;
    public String content;

    public Command(CommandType type, String content) {
        this.type = type;
        this.content = content;
    }
}
