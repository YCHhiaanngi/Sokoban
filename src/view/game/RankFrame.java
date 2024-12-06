package view.game;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RankFrame extends JFrame {

    private JTable rankTable;
    private DefaultTableModel tableModel;
    private int currentRankFileIndex = 1;
    private int maxRankFiles = 0; // 假设最大序号的rank文件数未知，后续会确定
    private JButton prevButton;
    private JButton nextButton;
    private JButton exitButton;

    public RankFrame() {
        // 设置窗口标题
        setTitle("Level: " + currentRankFileIndex);
        // 设置窗口大小
        setSize(600, 400);
        // 设置窗口关闭操作
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 创建表格模型
        tableModel = new DefaultTableModel();
        // 添加列名
        tableModel.addColumn("User Name");
        tableModel.addColumn("Steps");
        tableModel.addColumn("Time(Seconds)");
        tableModel.addColumn("Date");

        // 创建表格
        rankTable = new JTable(tableModel);

        //将表格数据居中排列
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        rankTable.setDefaultRenderer(Object.class, centerRenderer);


        // 将表格添加到滚动面板
        JScrollPane scrollPane = new JScrollPane(rankTable);
        getContentPane().add(scrollPane);

        // 创建上一页和下一页按钮
        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        exitButton = new JButton("Exit");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(exitButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // 为按钮添加监听器
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentRankFileIndex > 1) {
                    currentRankFileIndex--;
                    loadData();
                    setTitle("Level: " + currentRankFileIndex);
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentRankFileIndex < maxRankFiles) {
                    currentRankFileIndex++;
                    loadData();
                    setTitle("Level: " + currentRankFileIndex);
                }
            }
        });

        exitButton.addActionListener(e -> {
            this.setVisible(false);
        });

        // 确定最大序号的rank文件数
        findMaxRankFiles();

        // 加载第一个rank文件的数据
        loadData();

        // 显示窗口
        setVisible(true);
    }

    private void findMaxRankFiles() {
        File directory = new File("rank");
        File[] files = directory.listFiles();
        if (files!= null) {
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith("rank") && file.getName().endsWith(".txt")) {
                    int index = Integer.parseInt(file.getName().substring(4, file.getName().length() - 4));
                    maxRankFiles = Math.max(maxRankFiles, index);
                }
            }
        }
    }

    private void loadData() {
        // 清空表格模型中的现有数据
        tableModel.setRowCount(0);

        File file = new File("rank/rank" + currentRankFileIndex + ".txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;

            while ((line = br.readLine())!= null && count < 15) {
                String[] data = line.split(",");
                tableModel.addRow(data);
                count++;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}