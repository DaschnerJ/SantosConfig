/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package santosconfig;

import santoslogger.gui.Log;

/**
 *
 * @author justdasc
 */
public class SantosConfig {

    public static Log l;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        l = new Log( "config_log", "C:\\Users\\justdasc\\Desktop\\Santos\\", true);
        l.showLog();
        l.addInfo("Config log has been created.");
    }
    
}
