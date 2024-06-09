package udesc.dsd.Util;

import static udesc.dsd.Commons.Colors.*;

public interface Loggable {

    String EMPTY = "";

    default void log(Object msg) {
        System.out.println(msg); reset();
    }

    default void blueLog(Object msg) {
        System.out.println(BLUE + msg.toString()); reset();
    }

    default void redLog(Object msg) {
        System.out.println(RED + msg.toString()); reset();
    }

    default void greenLog(Object msg) {
        System.out.println(GREEN + msg.toString()); reset();
    }

    default void yellowLog(Object msg) {
        System.out.println(YELLOW + msg.toString()); reset();
    }

    default void purpleLog(Object msg) {
        System.out.println(PURPLE + msg.toString()); reset();
    }

    default void cyanLog(Object msg) {
        System.out.println(CYAN + msg.toString()); reset();
    }

    default void whiteLog(Object msg) {
        System.out.println(WHITE + msg.toString()); reset();
    }

    default void blackLog(Object msg) {
        System.out.println(BLACK + msg.toString()); reset();
    }

    private void reset(){System.out.print(RESET + EMPTY);}
}
