package com.thecubecast.ReEngine2.desktop;

public class test {

    //Numers higher than 13 when rolling 3 dice
    // 6 + 5 + 2
    // 6 + 6 + 1
    //etc

    public static void main(String[] args) {
        int count = 0;
        for(int x = 0; x <= 6; x++) {
            for(int y = 0; y <= 6; y++) {
                for(int z = 0; z <= 6; z++) {
                    if (x + y + z >= 16) {
                        System.out.println(x + " + " + y + " + " + z);
                        count++;
                    }
                }
            }
        }
        System.out.println(count);
    }
}
