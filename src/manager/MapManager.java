package manager;

import gamecore.Map;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapManager {

    private ArrayList<Map> mapList = new ArrayList<Map>();
    private String filename;

    public MapManager(int i) {

        if (i == 0) this.filename = "campaign.info";
        if (i == 1) this.filename = "custom.info";

        FileInputStream fileIS = null;
        try {
            fileIS = new FileInputStream("data/" + filename);
        } catch (FileNotFoundException ex) {
        }

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fileIS);
        } catch (IOException ex) {
        }

        try {
            try {
                mapList = (ArrayList<Map>) ois.readObject();
            } catch (IOException ex) {
            }
        } catch (ClassNotFoundException ex) {
        }

        try {
            ois.close();
        } catch (IOException ex) {
        }
    }

    public int getNewMapID() {
        return (this.mapList.size());
    }

    public void addNewMap(Map aMap) {
        ObjectOutputStream os = null;
        try {
            mapList.add(aMap);
            os = new ObjectOutputStream(new FileOutputStream("data/" + filename));
            os.writeObject(mapList);
            os.close();
        } catch (IOException ex) {
            Logger.getLogger(MapManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(MapManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void ChangeMap(int anID, Map aMap) {
        mapList.set(anID, aMap);
        
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new FileOutputStream("data/" + filename));
        } catch (IOException ex) {
        }
        try {
            os.writeObject(mapList);
        } catch (IOException ex) {
        }
        try {
            os.close();
        } catch (IOException ex) {
        }
    }

    public Map getMap(int anID) {
        return mapList.get(anID);
    }

    public ArrayList<Map> getMapList() {
        return mapList;
    }
}
