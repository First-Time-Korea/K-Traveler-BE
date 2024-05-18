package com.ssafy.firskorea.attraction.constant;

import java.util.List;

public enum PromptMessage {
    MESSAGE("%s___%s Translate into English."
            + "The one in '<>' is the title, '___'The first word is the name of the store."
            + " so make sure to put it in."
            + "For example: 'English store name___<Title> Description'"
            + "Don't mix your opinions, just convert Hangul into English.");

    private final String message;

    PromptMessage(String message) {
        this.message = message;
    }

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }

}
