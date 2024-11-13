package view.game;

public class AIResult {
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    String path;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    int length;

    AIResult(String path, int length) {
        this.path = path;
        this.length = length;
    }



}
