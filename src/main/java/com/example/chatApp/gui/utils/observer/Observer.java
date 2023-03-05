package com.example.chatApp.gui.utils.observer;

import com.example.chatApp.gui.utils.events.Event;

public interface Observer<E extends Event> {
    void update(String s);
}
