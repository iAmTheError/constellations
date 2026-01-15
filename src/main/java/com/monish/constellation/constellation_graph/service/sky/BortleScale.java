package com.monish.constellation.constellation_graph.service.sky;

public final class BortleScale {
    private BortleScale() {}

    public static double limitingMagnitude(int bortle){
        return switch (bortle) {
            case 1 -> 7.6;
            case 2 -> 7.1;
            case 3 -> 6.6;
            case 4 -> 6.1;
            case 5 -> 5.6;
            case 6 -> 5.1;
            case 7 -> 4.6;
            case 8 -> 4.1;
            case 9 -> 3.6;
            default -> throw new IllegalArgumentException("bortle must be between 1 and 9");
        };
    }
    //Buffer is 0.8 so we fetch slightly more dimmer stars also, just in case they may be visible
    public static double candidateMargin(){
            return 0.8;
    }
}
