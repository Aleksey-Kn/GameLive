import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.lang.Thread;

public class Game extends JFrame{
    private static int counter = 1;
    private JLabel generatinonLbl = new JLabel();
    private JLabel aliveLbl = new JLabel();

    public Game(boolean[][] now){
        super("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(425, 500);
        setVisible(true);
        setLayout(null);

        generatinonLbl.setBounds(15, 10, 160, 20);
        add(generatinonLbl);
        aliveLbl.setBounds(15, 30, 160, 20);
        add(aliveLbl);

        JPanel panel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                for (int i = 0; i < now.length; i++){
                    for(int j = 0; j < now[i].length; j++){
                        if(now[i][j]){
                            g.setColor(Color.WHITE);
                            g.fillRect(j * 20, i * 20, 19, 19);
                        }
                    }
                }
            }
        };
        panel.setBackground(Color.BLACK);
        panel.setLayout(null);
        panel.setBounds(0, 50, 400, 400);
        add(panel);
    }

    void setGenerationText(int value){
        generatinonLbl.setText("Generation #" + value);
    }

    void setAliveText(int value){
        aliveLbl.setText("Alive: " + value);
    }

    private static int saveNeghtbor(int i, int len){ // переход через границы мира
        if(i < len && i > 0){
            return i;
        }
        if(i < 0){
            return len - 1;
        }
        return 0;
    }

    private static int neghtborVel(boolean ths){return (ths? 1: 0);}

    private static void nextGeneration(boolean[][] now, boolean[][] past){
        counter = 0;
        for(int i = 0; i < now.length; i++){
            for(int j = 0, sum = 0; j < now.length; j++, sum = 0){
                sum += neghtborVel(now[saveNeghtbor(i - 1, now.length)][saveNeghtbor(j - 1, now.length)]);
                sum += neghtborVel(now[saveNeghtbor(i - 1, now.length)][saveNeghtbor(j, now.length)]);
                sum += neghtborVel(now[saveNeghtbor(i - 1, now.length)][saveNeghtbor(j + 1, now.length)]);
                sum += neghtborVel(now[saveNeghtbor(i, now.length)][saveNeghtbor(j - 1, now.length)]);
                sum += neghtborVel(now[saveNeghtbor(i, now.length)][saveNeghtbor(j + 1, now.length)]);
                sum += neghtborVel(now[saveNeghtbor(i + 1, now.length)][saveNeghtbor(j - 1, now.length)]);
                sum += neghtborVel(now[saveNeghtbor(i + 1, now.length)][saveNeghtbor(j, now.length)]);
                sum += neghtborVel(now[saveNeghtbor(i + 1, now.length)][saveNeghtbor(j + 1, now.length)]);
                if(sum == 2){
                    past[i][j] = now[i][j];
                    if(past[i][j]){
                        counter++;
                    }
                }
                else if(sum == 3){
                    past[i][j] = true;
                    counter++;
                }
                else{
                    past[i][j] = false;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        int it; // счётчик итераций здесь должан быть глобальным
        final int len = 20;
        final boolean[][] now = new boolean[len][len];
        final boolean[][] past = new boolean[len][len];
        final Random rand = new Random();
        final Game frame = new Game(now);
        for(int i = 0; i < len; i++){
            for(int j = 0; j < len; j++){
                now[i][j] = rand.nextBoolean();
            }
        }
        for(it = 0; counter > 0; it++){
            nextGeneration(now, past);
            for(int v = 0; v < now.length; v++){
                now[v] = past[v].clone();
            }
            frame.setGenerationText(it + 1);
            frame.setAliveText(counter);
            frame.repaint();
            Thread.sleep(750);
        }
    }
}