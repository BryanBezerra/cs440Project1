package org.introai;

import org.introai.bots.Bot;
import org.introai.bots.Bot1;
import org.introai.bots.Bot2;
import org.introai.bots.Bot3;

public class Main {
    public static void main(String[] args) {
        final int SHIP_SIZE = 70;
        final int RUNS = 50;
        double q;

        q = 0.1;
        System.out.println("Bot 1 @ size " + SHIP_SIZE);
        while (q <= 1.005) {
            int wins = 0;

            for (int i = 0; i < RUNS; i++) {
                Simulation sim = new Simulation(SHIP_SIZE, q);
                Bot bot = new Bot1(sim.getShipMap());
                boolean result = sim.run(bot);
                if (result) wins += 1;
            }
            System.out.println("At q = " + q + ", wins = " + wins + "/" + RUNS);
            q += 0.1;
        }

        q = 0.1;
        System.out.println("Bot 2 @ size " + SHIP_SIZE);
        while (q <= 1.005) {
            int wins = 0;

            for (int i = 0; i < RUNS; i++) {
                Simulation sim = new Simulation(SHIP_SIZE, q);
                Bot bot = new Bot2(sim.getShipMap());
                boolean result = sim.run(bot);
                if (result) wins += 1;
            }
            System.out.println("At q = " + q + ", wins = " + wins + "/" + RUNS);
            q += 0.1;
        }

        q = 0.1;
        System.out.println("Bot 3 @ size " + SHIP_SIZE);
        while (q <= 1.005) {
            int wins = 0;

            for (int i = 0; i < RUNS; i++) {
                Simulation sim = new Simulation(SHIP_SIZE, q);
                Bot bot = new Bot3(sim.getShipMap());
                boolean result = sim.run(bot);
                if (result) wins += 1;
            }
            System.out.println("At q = " + q + ", wins = " + wins + "/" + RUNS);
            q += 0.1;
        }
    }
}