import java.util.Scanner;
import java.util.Random;
import java.lang.Thread;

public class Game {
    private static int counter = 1;

    private static void print(char[][] mas){
        for(int i = 0; i < mas.length; i++){
            for(int j = 0; j < mas.length; j++){
                System.out.print(mas[i][j]);
            }
            System.out.println();
        }
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

    private static int neghtborVel(char ths){return (ths == 'O'? 1: 0);}

    private static void nextGeneration(char[][] now, char[][] past){
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
                    if(past[i][j] == 'O'){
                        counter++;
                    }
                }
                else if(sum == 3){
                    past[i][j] = 'O';
                    counter++;
                }
                else{
                    past[i][j] = ' ';
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        int it; // счётчик итераций здесь должан быть глобальным
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter world size: ");
        int len = scan.nextInt();
        char[][] now = new char[len][len];
        char[][] past = new char[len][len];
        //int forRand = scan.nextInt();
        //int kolGener = scan.nextInt();
        Random rand = new Random();
        for(int i = 0; i < len; i++){
            for(int j = 0; j < len; j++){
                now[i][j] = (rand.nextBoolean()? 'O': ' ');
            }
        }
        for(it = 0; counter > 0; it++){
            nextGeneration(now, past);
            for(int v = 0; v < now.length; v++){
                now[v] = past[v].clone();
            }
            System.out.println("\f");
            System.out.println("Generation number: " + (it + 1));
            System.out.println("Number of individuals: " + counter);
            print(now);
            Thread.sleep(1800);
        }
        System.out.println("Аll individuals died out! Civilization has lived " + it + "generation");
    }
}

