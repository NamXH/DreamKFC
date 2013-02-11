package manager;


import component.Conveyer;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ConveyerManager {

    private List<Conveyer> conveyerList;
    private int i, status;
    private int thisMapID, thisMapType;

    public ConveyerManager(int status, int anID, int mapType) {
        conveyerList = new ArrayList();
        this.status = status;
        thisMapID = anID;
        thisMapType = mapType;
    }

    public List<Conveyer> loaddb() {     
        int start, end;
        char c;
        String sub;
        int count;
        int x, y, direction;        

        File f;

        if (thisMapType == 1) {
            f = new File("Data/campaign/campaign"+ thisMapID +".map");
        } else {
            f = new File("Data/custom/custom"+ thisMapID +".map");
        }

        FileReader fr = null;
        try {
            fr = new FileReader(f);
            BufferedReader bf = new BufferedReader(fr);
            String s = null;
            while ((s = bf.readLine()) != null) {
                if ((s.charAt(0) != '#') && (s.contains("C"))) {
                    start = 0;
                    end = 0;
                    count = 1;
                    x = 0;
                    y = 0;
                    direction = 0;

                    while (end < s.length()) {
                        c = s.charAt(end);
                        while ((c != ' ') && (end < s.length() - 1)) {
                            end++;
                            c = s.charAt(end);
                        }

                        if (start == end) {
                            sub = s.substring(start, end + 1);
                        } else {
                            sub = s.substring(start, end);
                        }                        
                        switch (count) {
                            case 1: {
                                x = Integer.parseInt(sub);
                                break;
                            }
                            case 2: {
                                y = Integer.parseInt(sub);
                                break;
                            }
                            case 3: {
                                break;
                            }
                            case 4: {
                                direction = Integer.parseInt(sub);
                                break;
                            }
                        }
                        end++;
                        start = end;
                        count++;
                    }
                    conveyerList.add(new Conveyer(x, y, direction, status));
                }
            }
        } catch (Exception e) {
            System.out.println("error");
        }
        return conveyerList;
    }

    public Graphics2D draw(Graphics2D gbg) {
        for (int j = -19; j <= 19; j++) {
            for (i = 0; i < conveyerList.size(); i++) {
                Conveyer tem = conveyerList.get(i);
                if (tem.getZindex() == j) {
                    gbg.drawImage(tem.imgArr.getImg(), tem.getX(), tem.getY(), null);
                }
            }
        }
        return gbg;
    }

    public void update() {
    }
}
