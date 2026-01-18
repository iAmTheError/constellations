package com.monish.constellation.constellation_graph.service.sky;

public final class BortleScale {
    private BortleScale() {}

    public static double limitingMagnitude(int bortle){
        return switch (bortle) {
            case 1 -> 6.0;
            case 2 -> 5.6;
            case 3 -> 5.2;
            case 4 -> 5.0;
            case 5 -> 4.6;
            case 6 -> 4.2;
            case 7 -> 3.8;
            case 8 -> 3.3;
            case 9 -> 2.8;
            default -> throw new IllegalArgumentException("bortle must be between 1 and 9");
        };
    }
    //Buffer is 0.8 so we fetch slightly more dimmer stars also, just in case they may be visible
    public static double candidateMargin(){
            return 0.4;
    }
}
