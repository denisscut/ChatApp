package com.example.chatApp.gui.utils.observer;

import com.example.chatApp.gui.utils.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e, String str);
    void removeObserver(Observer<E> e, String str);
    void notifyObservers(E t, String str);
}