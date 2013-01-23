package Extras;

/**
 *
 * @author Isay
 */
public class Data {
    private int vId;
    private String vName;
    
    public Data(){};
    public Data(int id, String name){
        Id(id);
        Name(name);
    }
    
    public void Id(int value){ vId = value; }
    public int Id(){ return vId; }
    
    public void Name(String value){ vName = value; }
    public String Name(){ return vName; }        
    
    public String toString(){ return vName; }
}
