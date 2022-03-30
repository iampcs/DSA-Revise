package Heaps;

import java.util.Arrays;
import java.util.Comparator;

public class PlayWithComprators {
    public static void main(String[] args) {

        int[][] arr = new int[][] {{6,4,8},{9,4,3},{4,2,7}};

        sort2DArray(arr);

    }
    public static void print2DArray(int[][] arr){
        for(int row =0; row < arr.length; row++){
            for(int col = 0; col < arr[0].length; col++){
                System.out.print(arr[row][col] + " ");
            }
            System.out.println();
        }
    }

    public static void sort2DArray(int[][] arr){


        System.out.println("----------Sort a 2D array based on 2nd column increasing----------");
        Arrays.sort(arr, (o1, o2) -> o1[1] - o2[1]);
        print2DArray(arr);

        System.out.println("----------Sort a 2D array based on 2nd column increasing, if same 3rd column decreasing----------");
        Arrays.sort(arr, (o1, o2) -> {
            if(o1[1] != o2[1])
                return o1[1] - o2[1];
            return o2[2] - o1[2];
        });
        print2DArray(arr);

    }
}
