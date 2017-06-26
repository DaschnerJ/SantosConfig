/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package santosconfig.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import static santosconfig.SantosConfig.l;
import santosutils.file.FileManager;

/**
 *
 * @author justdasc
 */
public class Config {
    
    private final String extension = ".sanfig";
    
    private final FileManager f = new FileManager();
    
    private Properties properties;
    
    private FileInputStream in;
    private String name;
    private String location;
    
    public Config(String name)
    {
        this.name = name;
        this.location = f.GetExecutionPath() + "config\\"; 
        configInit();
        
    }
    
    public Config(String name, String location)
    {
        this.name = name;
        this.location = location;
        configInit();
    }
    
    public void configInit()
    {
        createConfig();
    }
    
    public boolean createConfig()
    {
        f.createDirectory(location);
        f.createFile(location + name + extension);
        File file = new File(location + name + extension);
        try {
            file.createNewFile();
            FileOutputStream oFile = new FileOutputStream(file, false);// if file already exists will do nothing 
            oFile.close();
        } catch (IOException ex) {
            l.addSevere(ex.getMessage());
        }
        return true;
    }
    
    
    
    
    
    
    
}
