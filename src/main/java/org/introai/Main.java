package org.introai;

import org.introai.bots.Bot;
import org.introai.bots.Bot1;
import org.introai.bots.Bot2;
import org.introai.bots.Bot3;

public class Main {
    public static void main(String[] args) {
        final int SHIP_SIZE = 30;
        final int RUNS = 500;
        final double Q_INTERVAL = 0.2;
        final double Q_START = 0.0;
        double q;

        q = Q_START;
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
            q += Q_INTERVAL;
        }

        q = Q_START;
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
            q += Q_INTERVAL;
        }

        q = Q_START;
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
            q += Q_INTERVAL;
        }
    }
}