package com;
import com.Counters.*;
import com.Fixnum.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    static public void main(String[] args) throws InterruptedException {
        Demonstration demo = new Demonstration();
        Boolean[] demonstrate = new Boolean[]{true, false, true, false, false, false};
                                            //  1     2      3      4      5      6

        for(int i = 0; i < 6; i++){
            if(demonstrate[i]){
                demo.task(i);
            }
        }
    }
}
