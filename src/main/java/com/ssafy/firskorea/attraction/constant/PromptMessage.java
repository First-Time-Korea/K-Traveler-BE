package com.ssafy.firskorea.attraction.constant;

public enum PromptMessage {
    MESSAGE("You are a translator who translates 1.attraction, 2.address, 3.media, and 4.description into English." +
            "Translate Korean into English. " +
            "When returning results, you must maintain the following structure as " +
            "___ is a separator, so separate each item by ___"+
            "'English translated attraction___English translated address___English translated media___English translated description' : " +
            "%s___%s___%s___%s");

    private final String message;

    PromptMessage(String message) {
        this.message = message;
    }

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }

}
