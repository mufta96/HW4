import java.util.Arrays;
import java.util.Scanner;

public class Game {
   public static int fieldsize =5;
    public static int winlenghth = 4;
    public static void main(String args[])
    {
        /*
        Игра крестики-нолики
        0*. размер поля
        0*. количество фишек
        1. нарисовать поле
        2. проверка победы
        3. ход игрока
        2. проверка победы
        4. ход компьютера
        4.1* ИИ
        5 проверка ничьи
         */


        char playersymbol='O';
        char AIsymbol='X';
        char[][] field = new char[fieldsize][fieldsize] ;
        for (char[] row : field)
            Arrays.fill(row, ' ');
        boolean game=false;
        int stepnum=0;
        do{
            //drawfield(field, fieldsize);
            playerturn(field,fieldsize, playersymbol);
            stepnum++;
            drawfield(field, fieldsize);
            if (wincheck(field,fieldsize,playersymbol))
            {
                System.out.println("Игрок победил!");
                break;
            }
            if (stepnum==fieldsize*fieldsize-1)
            {
                System.out.println("Ничья!");
                break;
            }
            System.out.println("\nХод ИИ\n");
            AIturn(field,fieldsize,AIsymbol,playersymbol);
            stepnum++;
            drawfield(field, fieldsize);
            if (wincheck(field,fieldsize,AIsymbol))
            {
                System.out.println("Скайнет победил!");
                break;
            }
            if (stepnum==fieldsize*fieldsize-1)
            {
                System.out.println("\nНичья!");
                break;
            }
        }
        while (!game);
        
    }
    //1. нарисовать поле
        /*
        поле n*n
        массив символов для значений крестиков и ноликов и пустых значений
        при отрисовке межу символами в строке знак "|"
        и между строками 2n-1 "_"
         */
    private static void drawfield(char[][] field, int n) {
        String delim = new String();
        for (int i = 0; i<2*n-1;i++){
            delim=delim+'_';
        }
        for (int i = 0; i<n;i++){
            for (int j = 0; j<n;j++){
                System.out.print(field[i][j]);
                if (j!=n-1)
                    System.out.print('|');
            }
            if (i!=n-1)
            System.out.println("\n" + delim);
        }
    }
/*
2. проверка победы
победа при комбинации из трех полей одинакового символа
в строку
в столбец
по диагонали
по обратной диагонали
 */
private static boolean wincheck(char[][] field, int n, char checksymbol){
    int temp, temp1, count;
    //horizontal
    for (int i = 0; i<n;i++) {
        for (int j = 0; j < n - winlenghth+1; j++) {
            temp=j;
            count = 0;
while (field[i][temp]==checksymbol){
temp++;
count++;
if (count==winlenghth)
    return true;
}
        }
    }
    //vertical
    for (int i = 0; i<n;i++) {
        for (int j = 0; j < n - winlenghth+1; j++) {
            temp=j;
            count = 0;
            while (field[temp][i]==checksymbol){
                temp++;
                count++;
                if (count==winlenghth)
                    return true;
            }
        }
    }
    //diagonal
    for (int i = 0; i<n-winlenghth+1;i++) {
        for (int j = 0; j < n-winlenghth+1; j++) {
            temp=j;
            temp1=i;
            count = 0;
            while (field[temp][temp1]==checksymbol){
                temp++;
                temp1++;
                count++;
                if (count==winlenghth)
                    return true;
            }
        }
    }
    // inverse diagonal
    for (int i = 0; i<n-winlenghth+1;i++) {
        for (int j = 3; j < n; j++) {
            temp=i;
            temp1=j;
            count = 0;
            while (field[temp][temp1]==checksymbol){
                temp++;
                temp1--;
                count++;
                if (count==winlenghth)
                    return true;
            }
        }
    }
    return false;
}
/*
3. ход игрока
опросить номер строки и номер столбца, поставить туда фигурку

проверить на число
проверить на корректность
проверить на наличие там фигурки
 */
private static void playerturn(char[][] field, int n, char playersymbol){
    int linenum=-1, colnum=-1;
    Scanner in = new Scanner(System.in);
    System.out.println("\nВаш ход.\n Введите номер столбца.\n");
    boolean check1=false;
    boolean check2=false;


    while (!check1)
    {
        while (!check2) {
            if (in.hasNextInt()) {
                colnum = in.nextInt();
                if (colnum > 0 & colnum < n + 1) {
                    colnum--;
                    check2=true;
                }
                else
                    System.out.println("Номер столбца выходит за рамки диапазона\n");
            }
            else {
                System.out.println("Это не число!\n");
                in.next();
            }
        }
        System.out.println("Введите номер строки\n");
        check2=false;
        while (!check2) {
            if (in.hasNextInt()) {
                linenum = in.nextInt();
                if (linenum > 0 & linenum < n + 1) {
                    linenum--;
                    check2 = true;
                } else
                    System.out.println("Номер строки выходит за рамки диапазона\n");
            } else{
                System.out.println("Это не число!\n");
            in.next();
        }
        }
        if (field[linenum][colnum]==' ')
        {
        field [linenum][colnum]=playersymbol;
        check1=true;
        }
        else
            System.out.println("На этом поле уже есть символ");
    }
}
/*
4. ход компьютера
        4.1* ИИ
Win: If the player has two in a row, they can place a third to get three in a row.

Block: If the opponent has two in a row, the player must play the third themselves to block the opponent.

Fork: Create an opportunity where the player has two ways to win (two non-blocked lines of 2).

Blocking an opponent's fork: If there is only one possible fork for the opponent, the player should block it. Otherwise, the player should block all forks in any way that simultaneously allows them to create two in a row. Otherwise, the player should create a two in a row to force the opponent into defending, as long as it doesn't result in them creating a fork. For example, if "X" has two opposite corners and "O" has the center, "O" must not play a corner move in order to win. (Playing a corner move in this scenario creates a fork for "X" to win.)

Center: A player marks the center. (If it is the first move of the game, playing a corner move gives the second player more opportunities to make a mistake and may therefore be the better choice; however, it makes no difference between perfect players.)

Opposite corner: If the opponent is in the corner, the player plays the opposite corner.

Empty corner: The player plays in a corner square.

Empty side: The player plays in a middle square on any of the 4 sides.
 */
private static char[][] AIturn(char[][] field, int n, char symbol, char playersymbol) {
    //win
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (field[i][j] == ' ') {
                field[i][j] = symbol;
                if (wincheck(field, n, symbol) != true)
                    field[i][j] = ' ';
                else
                    return field;
            }
        }
    }
    //block
    //
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (field[i][j] == ' ') {
                field[i][j] = playersymbol;
                if (wincheck(field, n, playersymbol) == true) {
                    field[i][j] = symbol;
                    return field;
                }
                else
                    {
                    field[i][j] = ' ';;
                }
                    
            }
        }
    }
    //center if center is empty play center
    if (field[1][1]==' '){
        field[1][1]=symbol;
        return field;
    }
    //opposite corner
    if (field[0][0]==playersymbol & field[n-1][n-1]==' '){
        field[n-1][n-1]=symbol;
        return field;
    }
    if (field[0][n-1]==playersymbol & field[n-1][0]==' '){
        field[n-1][0]=symbol;
        return field;
    }
    if (field[0][n-1]==playersymbol & field[n-1][0]==' '){
        field[n-1][0]=symbol;
        return field;
    }
    if (field[n-1][n-1]==playersymbol & field[0][0]==' '){
        field[0][0]=symbol;
        return field;
    }
    // Empty corner
    if (field[n-1][n-1]==' '){
        field[n-1][n-1]=symbol;
        return field;
    }
    if ( field[n-1][0]==' '){
        field[n-1][0]=symbol;
        return field;
    }
    if (field[n-1][0]==' '){
        field[n-1][0]=symbol;
        return field;
    }
    if ( field[0][0]==' '){
        field[0][0]=symbol;
        return field;
    }
    // side
    int i=0;
    for (int j = 1; i<n-1; i++){
        if (field[i][j]==' '){
            field[i][j]=symbol;
            return field;
        }
    }
    i=n-1;
    for (int j = 1; i<n-1; i++){
        if (field[i][j]==' '){
            field[i][j]=symbol;
            return field;
        }
    }
    int j=0;
    for ( i = 1; i<n-1; i++){
        if (field[i][j]==' '){
            field[i][j]=symbol;
            return field;
        }
    }
    j=n-1;
    for ( i = 1; i<n-1; i++){
        if (field[i][j]==' '){
            field[i][j]=symbol;
            return field;
        }
    }
    return field;
}
}
