package com.leonkianoapps.d4a.leonard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LoadDateFromCache {

    private File file;


    public LoadDateFromCache(File file) {
        this.file = file;
    }


    public String loadInfo(){

        FileInputStream fileInputStream = null;

        try {  //To prevent from reading from a file that does't exist

            fileInputStream = new FileInputStream(file);  //Reading from the given file

            int read = -1;

            StringBuilder stringBuffer = new StringBuilder();  //to append all data gotten from the file

            while ((read = fileInputStream.read()) != -1)    //returns an int if its -1 mean its empty
            {
                stringBuffer.append((char) read); //will contain ASCA value in numbers  to cast char will change the numbers to characters


            }

            return stringBuffer.toString();   //Data was got and returning here!


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {

                if (fileInputStream != null) {

                    fileInputStream.close();

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "Something went wrong";



    }


}
