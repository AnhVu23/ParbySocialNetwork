/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

/**
 *
 * @author GamingPc
 */
public class TextUtils {
    public static boolean isEmpty(String text){
     if (text==null || text.equals("")) {
         return true;
     } else {
         return false;
     } 
    }
    
    public static boolean isEmpty(String ... text){
        for(String t: text) {
            if (t == null || t.equals("")) 
            return true; 
        }
        return false;
    }
}
