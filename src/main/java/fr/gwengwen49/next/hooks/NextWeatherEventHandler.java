package fr.gwengwen49.next.hooks;

public interface NextWeatherEventHandler {

    default boolean isNebulaEvent() {
        return false;
    }

    default boolean isFallingStarsEvent() {
        return false;
    }

    default void weatherTick(){}

    default void startNebulaEvent(boolean value){

    }

    default void startFallingStarsEvent(boolean value){

    }
}
