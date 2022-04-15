/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author larby`    */
public class NtuModHelper {
    public static Reader getReader() {

        try {
            String filepath = NtuModHelper.class.getClassLoader().getResource("").getPath() + "../../docroot/ntuList.json";
            System.out.println(filepath);
            File modFile = new File(filepath);
            InputStream stream = new FileInputStream(modFile);
            return new BufferedReader(new InputStreamReader(stream));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NtuModHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NtuModHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
