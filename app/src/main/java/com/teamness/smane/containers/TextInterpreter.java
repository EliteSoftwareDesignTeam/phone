package com.teamness.smane.containers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextInterpreter {

    private ArrayList<String> GOcommands;
    private ArrayList<String> STOPcommands;
    private ArrayList<String> SAVEcommands;

    public TextInterpreter(){
        init();
    }

    private void init()
    {
        GOcommands=new ArrayList<>();
        GOcommands.add("bring me (to )?");
        GOcommands.add("show me the way (to )?");
        GOcommands.add("navigate (to )?");
        GOcommands.add("take me (to )?");
        GOcommands.add("how do I get (to )?");
        GOcommands.add("guide me (to )?");
        GOcommands.add("go to ");
        GOcommands.add("give me directions to ");

        STOPcommands=new ArrayList<>();
        STOPcommands.add("stop");
        STOPcommands.add("cancel");

        SAVEcommands=new ArrayList<>();
        SAVEcommands.add("(save|send|record|memorize|remember) this (location|place|destination) as ");
    }

    /**
     * This method associates a sentence with a series of known commands
     * @param texts A list of String that represent the spoken sentence
     * @return The command sting associated
     */
    public Command Interpret(List<String> texts){
        Command command=new Command(CommandType.NOT_IMPLEMENTED,"");
        boolean matched=false;
        //Take each result obtained from speech rec, the most likely first
        for(String text:texts){
            text=text.toLowerCase();
            System.out.println("TI: testing the text: "+text);
            //Test for "go to" commands:
            for(String regex:GOcommands) {
                Pattern pattern = Pattern.compile(regex);
                Matcher m = pattern.matcher(text);
                if (m.find()) {
                    System.out.println("TI: " + "GO command:");
                    String dest = m.replaceFirst("");
                    System.out.println("TI: " + dest);
                    command.reInit(CommandType.DIRECTIONS,dest);
                    matched = true;
                    break;
                }
            }
            if(matched)break;
            for (String regex : SAVEcommands) {
                Pattern pattern = Pattern.compile(regex);
                Matcher m = pattern.matcher(text);
                if (m.find()) {
                    System.out.println("TI: " + "SAVE command:");
                    String dest = m.replaceFirst("");
                    System.out.println("TI: " + dest);
                    command.reInit(CommandType.SAVE_LOCATION,dest);
                    matched = true;
                    break;
                }
            }
            if(matched)break;
            for (String regex : STOPcommands) {
                Pattern pattern = Pattern.compile(regex);
                Matcher m = pattern.matcher(text);
                if (m.find()) {
                    System.out.println("TI: " + "STOP command");
                    command.reInit(CommandType.CANCEL_DIRECTIONS,"");
                    matched = true;
                    break;
                }
            }
            if(matched)break;
        }
        return command;
    }



}
