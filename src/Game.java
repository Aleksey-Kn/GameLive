import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;
import java.lang.Thread;

public class Game extends JFrame{
    private int counter = 1;
    private JLabel generationLbl = new JLabel();
    private JLabel aliveLbl = new JLabel();
    private boolean plaing;
    private int it = 1;
    private int speed = 1250;

    public Game(){
        super("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(425, 620);
        setVisible(true);
        setLocation(300, 100);
        setLayout(null);

        final int len = 20;
        boolean[][] now = new boolean[len][len];
        final boolean[][] past = new boolean[len][len];
        final Random rand = new Random();
        newBoard(now, rand);
        count(now);
        plaing = false;

        JLabel strGeneration = new JLabel();
        strGeneration.setText("Generation #");
        strGeneration.setBounds(15, 10, 80, 20);
        add(strGeneration);

        generationLbl.setBounds(105, 10, 160, 20);
        generationLbl.setName("GenerationLabel");
        generationLbl.setText("1");
        add(generationLbl);

        JLabel strAlive = new JLabel();
        strAlive.setText("Alive:");
        strAlive.setBounds(15, 30, 55, 20);
        add(strAlive);

        aliveLbl.setBounds(70, 30, 160, 20);
        aliveLbl.setName("AliveLabel");
        setAliveText(counter);
        add(aliveLbl);

        JSlider slider = new JSlider(0, 1950, 1250);
        slider.setBounds(20, 135, 385, 20);
        slider.addChangeListener(e -> speed = ((JSlider)e.getSource()).getValue());
        add(slider);

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
        panel.setBounds(0, 170, 400, 400);
        add(panel);

        JButton play = new JButton("Play");
        play.setName("PlayToggleButton");
        play.setBounds(135, 70, 110, 50);
        play.addActionListener(e -> plaing = true);
        add(play);

        JButton reset = new JButton("Reset");
        reset.setName("ResetButton");
        reset.setBounds(10, 70, 110, 50);
        reset.addActionListener(e ->{
            newBoard(now, rand);
            count(now);
            setAliveText(counter);
            setGenerationText(1);
            repaint();
            plaing = false;
            it = 1;
        });
        add(reset);

        JButton pause = new JButton("Pause");
        pause.setBounds(260, 70, 110, 50);
        pause.addActionListener(e -> plaing = false);
        add(pause);

        while (counter > 0){
            if(plaing) {
                nextGeneration(now, past);
                for (int v = 0; v < now.length; v++) {
                    now[v] = past[v].clone();
                }
                setGenerationText(it + 1);
                setAliveText(counter);
                repaint();
                it++;
            }
            try {
                Thread.sleep(2000 - speed);
            }
            catch (InterruptedException e){
                break;
            }
        }
    }

    void setGenerationText(int value){
        generationLbl.setText(Integer.toString(value));
    }

    void setAliveText(int value){
        aliveLbl.setText(Integer.toString(value));
    }

    private int saveNeghtbor(int i, int len){ // переход через границы мира
        if(i < len && i > 0){
            return i;
        }
        if(i < 0){
            return len - 1;
        }
        return 0;
    }

    private int neghtborVel(boolean ths){return (ths? 1: 0);}

    private void nextGeneration(boolean[][] now, boolean[][] past){
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

    private void newBoard(boolean[][] mat, Random rand){
        for(int i = 0; i < mat.length; i++){
            for(int j = 0; j < mat.length; j++){
                mat[i][j] = rand.nextBoolean();
            }
        }
    }

    private void count(boolean[][] mat){
        counter = 0;
        for(int i = 0; i < mat.length; i++){
            for(int j = 0; j < mat.length; j++){
                if(mat[i][j]){
                    counter++;
                }
            }
        }
    }

    public static void main(String[] args) {
        final Game frame = new Game();
    }
}