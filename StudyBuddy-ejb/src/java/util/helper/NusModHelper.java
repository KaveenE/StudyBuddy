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
 * @author larby
 */
public class NusModHelper {
    public static Reader getReader() {
//        Not working because of some HTTPS shit
        
//        try {
//            LocalDate today = LocalDate.now();
//            String acaYear;
//            if (today.getMonthValue() < 6) {
//                acaYear = String.format("%d-%d", today.getYear() - 1, today.getYear());
//            } else {
//                acaYear = String.format("%d-%d", today.getYear(), today.getYear() + 1);
//            }
//            String urlString = String.format("https://api.nusmods.com/v2/%s/moduleList.json", acaYear);
//            URL url = new URL(urlString);
//            
//            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//            int responseCode = con.getResponseCode();
//            if (responseCode < 300) {
//                System.out.println("Successful connection");
//                BufferedReader in = new BufferedReader(
//                        new InputStreamReader(con.getInputStream()));
//                
//                return in;
//            } else {
//                System.out.println("We failed");
//            }
//
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(NusModHelper.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(NusModHelper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return null;

        try {
            String filepath = NusModHelper.class.getClassLoader().getResource("").getPath() + "../../docroot/moduleList.json";
            System.out.println(filepath);
            File modFile = new File(filepath);
            InputStream stream = new FileInputStream(modFile);
            return new BufferedReader(new InputStreamReader(stream));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NusModHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NusModHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
