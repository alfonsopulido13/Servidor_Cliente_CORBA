package BaseDatos;
import java.sql.*;
import javax.swing.JOptionPane;

public class BDMySQL {
    private String db, ip_loc, path, user, pwd;
    private Connection con;    
       
    public BDMySQL(){        
        AbrirConexion();
    }
    
    public BDMySQL(String db, String ip_loc, String user, String pwd){
        this.db = db;
        this.ip_loc = ip_loc;
        path = "jdbc:mysql://" + ip_loc + "/" + db;
        this.user = user;
        this.pwd = pwd;
        con = Conectar();
    }
    
    public void AbrirConexion(){
        setDB("pmsdb");
        setIP_LOC("localhost");
        setPath("jdbc:mysql://" + getIP_LOC() + "/" + getDB());
        setUser("root");
        setPwd("fth5j32l");
        setCon(Conectar());
    }
    
    public Connection Conectar(){
        Connection link = null;
        try{                        
            Class.forName("com.mysql.jdbc.Driver");
            link = DriverManager.getConnection(path, user, pwd);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "Error al Conectar", JOptionPane.ERROR_MESSAGE);
        }
        
        return link;
    }        
    
    public void setDB(String db){ this.db = db; }
    public String getDB(){ return db; }
    
    public void setIP_LOC(String ip_loc){ this.ip_loc = ip_loc; }
    public String getIP_LOC(){ return ip_loc; }
    
    public void setPath(String path){ this.path = path; }
    public String getPath(){ return path; }
    
    public void setUser(String user){ this.user = user; }
    public String getUser(){ return user; }
    
    public void setPwd(String pwd){ this.pwd = pwd; }
    public String getPwd(){ return pwd; }
    
    public void setCon(Connection con){ this.con = con; }
    public Connection getCon(){ return con; }        
    
    public void CerrarConexion(){ 
        try{
            con.close(); 
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "Error al cerrar la conexión", JOptionPane.ERROR_MESSAGE);
        }        
    }                                                  
}