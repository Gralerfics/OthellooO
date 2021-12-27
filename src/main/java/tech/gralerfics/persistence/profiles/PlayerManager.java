package tech.gralerfics.persistence.profiles;

import tech.gralerfics.gamelogic.Player;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public final class PlayerManager {
    public static final String path = "assets/players";

    public static HashMap<String, Player> list = new HashMap<>();

    public static void modifyPlayer(Player p, String newName, Color color) {
        Player np = new Player(p);
        np.name = newName;
        if (color != null) np.color = color;
        deletePlayer(p);
        newPlayer(np);
    }

    public static void deletePlayer(Player p) {
        try {
            File file = new File(path + "/" + p.name + ".otpl");
            if (file.isFile() && file.exists()) {
                file.delete();
            }
            list.remove(p.name);
        } catch (Exception e) {
            // …æ≥˝ player π ’œ
        }
    }

    public static void savePlayer(Player p) {
        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(path + "/" + p.name + ".otpl"));
            fw.write(p.name + "\n");
            fw.write(p.tag + "\n");
            fw.write(p.wins + "\n");
            fw.write(p.fines + "\n");
            fw.write(p.fails + "\n");
            fw.write(p.color.getRed() + " " + p.color.getGreen() + " " + p.color.getBlue() + "\n");
            fw.close();
        } catch (IOException e) {
            // ±£¥Ê player π ’œ
        }
    }

    public static void newPlayer(Player p) {
        list.put(p.name, p);
        savePlayer(p);
    }

    public static void loadPlayers() {
        list.clear();
        File[] fs = (new File(path)).listFiles();
        if (fs != null) {
            for (File f : fs) {
                try {
                    Player tp = new Player();
                    tp.nextStep = -1;

                    BufferedReader fin = new BufferedReader(new FileReader(f));
                    int peek;
                    StringBuilder sin = new StringBuilder();
                    while ((peek = fin.read()) != -1) sin.append((char) peek);
                    fin.close();
                    String prs = sin.toString();
                    Scanner sc = new Scanner(prs);

                    tp.name = sc.nextLine();
                    if (!tp.name.equals(f.getName().replaceAll("[.][^.]+$", ""))) throw new Exception();
                    tp.tag = sc.nextInt();
                    tp.wins = sc.nextInt();
                    tp.fines = sc.nextInt();
                    tp.fails = sc.nextInt();
                    int r = sc.nextInt(), g = sc.nextInt(), b = sc.nextInt();
                    if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) throw new Exception();
                    tp.color = new Color(r, g, b);

                    list.put(tp.name, tp);
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }
}
