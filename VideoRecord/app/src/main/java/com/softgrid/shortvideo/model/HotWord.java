package com.softgrid.shortvideo.model;

import java.util.Random;

/**
 * Created by tianfeng on 2018/8/22.
 */

public class HotWord {

    private String id;
    private String word;

    public HotWord(boolean test){
        if (test){
            int random = new Random().nextInt(3);
            if (random == 1){
                word = "近地铁";
            }
            else if(random == 2){
                word = "学区房";
            }
            else {
                word = "配套成熟";
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
