package com.cz.android.sample.core;

import org.junit.Test;

import java.io.File;

/**
 * @author Created by cz
 * @date 2020-01-27 18:02
 * @email bingo110@126.com
 */
public class FileTest {

    /**
     * print all the source in project
     */
    @Test
    public void printProjectFile(){
        File file=new File("src/main/java");
        printFile(file,0);
    }

    public void printFile(File file,int level){
        StringBuilder output=new StringBuilder();
        for(int i=0;i<level;i++){
            output.append('\t');
        }
        output.append("|--"+file.getName());
        System.out.println(output.toString());
        if(file.isDirectory()){
            for(File f:file.listFiles()){
                printFile(f,level+1);
            }
        }
    }
}
