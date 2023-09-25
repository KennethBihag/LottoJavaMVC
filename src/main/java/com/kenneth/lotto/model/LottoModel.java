package com.kenneth.lotto.model;

import java.util.Random;

public interface LottoModel {
    int maxPicks=6, minPick=1, maxPick=45;

    static void randomize(int[] input,int start){
        do {
            for (int i = start; i < input.length; ++i) {
                Random random = new Random();
                input[i] = random.nextInt(45) + 1;
            }
        }while(start<input.length-1 && hasDuplicates(input,start));
    }

    static boolean hasDuplicates(int[] input,int start){
        if(start<0 | start >= input.length-1)
            throw new IllegalArgumentException(
                    "Start index must be from 0 to end-1.");
        for(int i=start; i < input.length-1; ++i)
            for(int j=i+1; j<input.length;++j)
                if(input[i]==input[j])
                    return true;
        return false;
    }

    int[] getPicks();

    default String getPicksString(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<maxPicks;++i) {
            sb.append(getPicks()[i]);
            if(i<maxPicks-1)
                sb.append('-');
        }
        return sb.toString();
    }

    default boolean hasDuplicates(){
        return hasDuplicates(getPicks(),0);
    }
}
