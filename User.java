

import javax.swing.JOptionPane;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class User {
    private ArrayList<String> songs = new ArrayList<String>();
    private ArrayList<String> artists = new ArrayList<String>();
    private ArrayList<String> dates = new ArrayList<String>();
    private ArrayList<String> comments = new ArrayList<String>();
    private ArrayList<Integer> scores = new ArrayList<Integer>();

    private final String DATA_FILE = "songs_data.csv";
    private final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");

    public User() {
        DATE_FMT.setLenient(false);
        loadData();
    }

    // ===== 校验与工具 =====
    public boolean validName(String s) {
        return s != null && s.trim().length() > 0 && s.trim().length() <= 60;
    }

    public boolean validDate(String s) {
        try {
            DATE_FMT.parse(s.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private long daysBetween(String d1, String d2) {
        try {
            long t1 = DATE_FMT.parse(d1).getTime();
            long t2 = DATE_FMT.parse(d2).getTime();
            return (t2 - t1) / (1000L * 60L * 60L * 24L);
        } catch (ParseException e) {
            return 0L;
        }
    }

    private int indexOfIgnoreCase(ArrayList<String> list, String target) {
        if (target == null) return -1;
        for (int i = 0; i < list.size(); i++) {
            if (target.equalsIgnoreCase(list.get(i))) return i;
        }
        return -1;
    }

    // ===== 数据操作 =====
    public boolean addSong(String title, String artist, String date) {
        if (!validName(title) || !validName(artist) || !validDate(date)) return false;
        songs.add(title);
        artists.add(artist);
        dates.add(date);
        comments.add("");
        scores.add(null);
        saveData();
        return true;
    }

    public String commentSong(String title, Integer score, String comment) {
        int idx = indexOfIgnoreCase(songs, title);
        if (idx == -1) return "Cannot find the song.";
        if (score == null || score < 0 || score > 10) return "分数须为0–10的整数。";
        if (comment == null || comment.trim().isEmpty()) return "评论不能为空。";
        if (comment.length() > 200) return "评论过长（>200）。";
        scores.set(idx, score);
        comments.set(idx, comment);
        saveData();
        return null;
    }

    public Object[][] buildDisplayTable(String currentDate) {
        int n = songs.size();
        long[] durations = new long[n];
        for (int i = 0; i < n; i++) durations[i] = daysBetween(dates.get(i), currentDate);

        int[] order = new int[n];
        for (int i = 0; i < n; i++) order[i] = i;

        for (int i = 0; i < n - 1; i++) {
            int max = i;
            for (int j = i + 1; j < n; j++) {
                if (durations[order[j]] > durations[order[max]]) max = j;
            }
            int tmp = order[i];
            order[i] = order[max];
            order[max] = tmp;
        }

        Object[][] data = new Object[n][6];
        for (int r = 0; r < n; r++) {
            int i = order[r];
            data[r][0] = r + 1;
            data[r][1] = songs.get(i);
            data[r][2] = artists.get(i);
            data[r][3] = dates.get(i);
            data[r][4] = (scores.get(i) == null ? "-" : scores.get(i));
            data[r][5] = durations[i];
        }
        return data;
    }

    public String[] getCommentByRow(Object[][] tableData, int row) {
        if (row < 0 || row >= tableData.length) return null;
        String title = (String) tableData[row][1];
        int idx = indexOfIgnoreCase(songs, title);
        if (idx == -1) return null;
        String comment = comments.get(idx);
        Integer score = scores.get(idx);
        return new String[]{
                title,
                comment.isEmpty() ? "(No comment yet)" : comment,
                score == null ? "No score yet" : score.toString()
        };
    }

    // ===== 文件存取 =====
    private void saveData() {
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(DATA_FILE), "UTF-8"))) {
            for (int i = 0; i < songs.size(); i++) {
                String score = (scores.get(i) == null ? "" : scores.get(i).toString());
                out.println(songs.get(i) + "|" + artists.get(i) + "|" + dates.get(i) + "|" + score + "|" + comments.get(i));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "保存失败：" + e.getMessage());
        }
    }

    private void loadData() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length < 5) continue;
                songs.add(p[0]);
                artists.add(p[1]);
                dates.add(p[2]);
                scores.add(p[3].isEmpty() ? null : Integer.valueOf(p[3]));
                comments.add(p[4]);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "读取失败：" + e.getMessage());
        }
    }
}
