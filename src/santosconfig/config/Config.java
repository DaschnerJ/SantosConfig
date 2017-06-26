/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package santosconfig.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static santosconfig.SantosConfig.l;
import santosutils.file.FileManager;

/**
 *
 * @author justdasc
 */
public class Config {

    //The extension of the Santos Config files.
    private final String extension = ".sanfig";

    //Create a file manager to be able to create the initial directories and files.
    private final FileManager f = new FileManager();

    //The name of the config file.
    private String name;

    //The location of the config file.
    private String location;

    //We use Java Properties to easily store and retrieve variables.
    private Properties p = new Properties();

    //We need an output stream in order to write to the properties file.
    private OutputStream output = null;

    //We need an input stream in order to read from the properties file.
    private InputStream input = null;

    /**
     * Create a config file in a default location with the desired name.
     * @param name The name of the config file.
     */
    public Config(String name) {
        this.name = name;
        //We append the config\\ on the end to put the config in the default config folder.
        this.location = f.GetExecutionPath() + "\\config\\";
        //We initialize the config with this method to reduce redundancy.
        configInit();
    }

    /**
     * Create a config file in the desired location and name.
     * @param name Name of the config file.
     * @param location Location of the config file.
     */
    public Config(String name, String location) {
        this.name = name;
        this.location = location;
        //We initialize the config with this method to reduce redundancy.
        configInit();
    }

    /**
     * Initializes the basic parts of the config file such as creating or
     * refreshing the config file.
     */
    private void configInit() {
        //We first create the config if the config or directory doesn't exist.
        createConfig();
    }

    /**
     * Writes a value to the config file with the key.
     * This will overwrite already existing properties.
     * @param key The key of where to write the value to.
     * @param value The value to store to the file.
     */
    public void writeValue(String key, String value) {
        try {
            //We try to create a file output stream for the currently file.
            output = new FileOutputStream(location + name + extension);
            //We set the property value here.
            p.setProperty(key, value);
            //We store the value and save it to the file.
            p.store(output, null);
        } catch (FileNotFoundException e) {
            //If this fails, print out the error for debugging.
            l.addSevere(e.getMessage());
        } catch (IOException e) {
            //If this fails, print out the error for debugging.
            l.addSevere(e.getMessage());
        } finally {
            if (output != null) {
                try {
                    //We close the output stream because we are no longer using it.
                    output.close();
                } catch (IOException e) {
                    //If this fails, print out the error for debugging.
                    l.addSevere(e.getMessage());
                }
            }
        }
    }

    /**
     * Loads the value of the key from the config file. If the key doesn't
     * exist, this method returns null. Using safeLoad is recommended instead.
     * @param key The to load the value from.
     * @return Returns the value from the key or null if the key doesn't exist.
     */
    public String loadValue(String key) {
        //We set null incase the key doesn't exist. This should have been checked first.
        String r = null;
        try {

            //Create the new input stream to read from.
            input = new FileInputStream(location + name + extension);

            //Load the properties file.
            p.load(input);

            //Get the property and set it equal to r so it can be returned.
            r = p.getProperty(key);

        } catch (IOException e) {
            //If any of this failed, print the error for debugging.
            l.addSevere(e.getMessage());
        } finally {
            //If the input is still reading, then we must close it.
            if (input != null) {
                try {
                    //Closing the input properly.
                    input.close();
                } catch (IOException e) {
                    //If any of this fialed, print the error for debugging.
                    l.addSevere(e.getMessage());
                }
            }
        }
        //We have what we need, lets return the selected value.
        return r;
    }
    
    /**
     * Checks if they key exists within the config file. If the key does exist
     * then this method returns true.
     * @param key The key to check existence of.
     * @return Returns true if the key exists or false if it does not.
     */
    public boolean checkValue(String key)
    {
        //Set this to false so if the key doesn't exist then the method will just return false.
        boolean r = false;
        try {

            //Create the new input stream to read from.
            input = new FileInputStream(location + name + extension);

            //Load the properties file.
            p.load(input);

            //Get the property and set it equal to r so it can be returned.
            r = p.containsKey(key);

        } catch (IOException e) {
            //If any of this failed, print the error for debugging.
            l.addSevere(e.getMessage());
        } finally {
            //If the input is still reading, then we must close it.
            if (input != null) {
                try {
                    //Closing the input properly.
                    input.close();
                } catch (IOException e) {
                    //If any of this fialed, print the error for debugging.
                    l.addSevere(e.getMessage());
                }
            }
        }
        //We have what we need, lets return the selected value.
        return r;
    }

    /**
     * This method safely writes values with the desired keys. If the key exists
     * then the request will be ignored.
     * @param key The key you wish to write the value to.
     * @param value The value you wish to write to the config.
     */
    public void safeWrite(String key, String value)
    {
        //If the key doesn't exist.
        if(!checkValue(key))
            writeValue(key, value);// Then write it.
    }
    
    /**
     * Safely loads data from the config. If the key fails to exist then this
     * method will return 'none'.
     * @param key The key for the value you wish to load from config.
     * @return Returns the value associated with the key or returns 'none' if no key is found.
     */
    public String safeLoad(String key)
    {
        //If the key does exist.
        if(checkValue(key))
            return loadValue(key);//Then load the value.
        else return "none";//Otherwise return 'non'.
    }
    
    /**
     * Safely loads the integer from the config. If the key fails to exist then
     * this method will return 0.
     * @param key The key for the integer you wish to load from config.
     * @return Returns the integer associated with the key or returns 0 if no key is found.
     */
    public Integer safeLoadInteger(String key)
    {
        //Set the default value to return 0.
        Integer r = 0;
        //If the key does exist.
        if(checkValue(key))
        {
            /**
             * We obtain the value from the string. We use safeLoad to ensure
             * we get a value and not null from the key.
             */
            String s = safeLoad(key);
            //We remove all non numeric characters and signs.
            s = s.replaceAll("[^\\d-]", "");
            //Incase we stripped everything, replace it with the default 0.
            if(s.equals(""))
                s = "0";
            //We then convert the string to an integer.
            r = Integer.valueOf(s);
        }
        //Return the integer value.
        return r;  
    }
    
    public Boolean safeLoadBoolean(String key)
    {
        //Set the default value to return false.
        Boolean r = false;
        //Check if the key exists.
        if(checkValue(key))
            /**
             * We then parse the obtained value from the key. Note we use
             * safeLoad to ensure we get a value from the key and not null.
             */
            r = Boolean.parseBoolean(safeLoad(key));
        //Then return the boolean we obtained.
        return r;
    }
    
    /**
     * Creates the config file and directory if they do not exist.
     * @return Returns true if file and directory were successfully created.
     */
    private boolean createConfig() {
        //We first create the directory for the config file if it doesn't exist.
        f.createDirectory(location);
        //We then create a file at the location with the extension.
        f.createFile(location + name + extension);
        //Returns true because we successfully created the file.
        return true;
    }

}
