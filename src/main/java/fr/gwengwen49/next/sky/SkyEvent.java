package fr.gwengwen49.next.sky;

public interface SkyEvent {

    String getName();

    default void onClientWorldTick(){

    }
}
