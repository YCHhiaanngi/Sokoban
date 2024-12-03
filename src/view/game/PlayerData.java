package view.game;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static view.level.LevelFrame.getCurrentLevel;

public class PlayerData implements Comparable<PlayerData>{//辅助类，保存游戏数据，用于排行
    private double time;
    private String username;
    private int step;

    public PlayerData(double time, String username, int step){
        this.time = time;
        this.username = username;
        this.step = step;
    }

    public void updateData() throws IOException {//更新排行数据
        File file = new File("rank/rank"+getCurrentLevel()+".txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        List<PlayerData> rank = new ArrayList<>();
        while((line = br.readLine()) != null){
            double time = Double.parseDouble(line.split(",")[2]);
            String username = line.split(",")[0];
            int step = Integer.parseInt(line.split(",")[1]);
            rank.add(new PlayerData(time,username,step));
        }
        rank.add(this);
        Collections.sort(rank);
        System.out.println(rank);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < rank.size(); i++) {
            bw.write(rank.get(i).toString());
            bw.write('\n');
        }
        bw.close();
        br.close();
    }

    @Override
    public int compareTo(PlayerData data){
        return this.step-data.step;
    }

    @Override
    public String toString(){
        return this.username+","+this.step+","+this.time;
    }
//测试代码
//    public static void main(String[] args) {
//        PlayerData data = new PlayerData(1999,"mayuxin",35);
//        try {
//            data.updateData();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
