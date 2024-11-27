package view.game.clock;

import javax.swing.*;
import java.awt.*;

public class Clock {
        private Display minute=new Display(60);
    private Display seconds=new Display(60);
    public void start(){
        seconds.increase();
        if(seconds.getValue()==0){
            minute.increase();
        }
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d\n", minute.getValue(), seconds.getValue());
    }
}