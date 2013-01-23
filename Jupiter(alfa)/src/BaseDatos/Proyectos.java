package BaseDatos;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author Isay
 */
public class Proyectos extends BDMySQL{
    public Proyectos(){ super(); }        
    
    public Proyectos(String db, String ip_loc, String user, String pwd){ super(db, ip_loc, user, pwd); }
        
    private String find(String nombre, byte estado){
        String dato;
        String sSQL = "SELECT * FROM pms_projects WHERE Name = '" + nombre + "' AND State = " + estado;
        
        try{            
            Statement st = getCon().createStatement();                        
            ResultSet rs = st.executeQuery(sSQL);
            
            rs.first();
            dato = rs.getString("ID_Project");
        }catch(Exception e){
            dato = "";            
        }        
        CerrarConexion();        
        return dato;
    }
    
    public boolean save(String[] datos){
        if(!find(datos[1], (byte)1).equals("")){            
            CerrarConexion();
            return false;
        }else AbrirConexion();
        
        String sSQL = "INSERT INTO pms_projects(ID_User, Name, Description, Start_Date, "
                + "End_Date, State) VALUES(?, ?, ?, ?, ?, 1)";
                        
        try {            
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ps.setInt(1, Integer.parseInt(datos[0]));
            ps.setString(2, datos[1]);
            ps.setString(3, datos[2]);
            ps.setString(4, datos[3]);
            ps.setString(5, datos[4]);
            ps.execute();
            CerrarConexion();
            return true;            
        } catch (Exception ex) {
            CerrarConexion();
            return false;
        }
    }    
    
    public String[][]get(String busqueda, byte estado){
        String[][] registros = null;               
        String sSQL = "SELECT pms_users.`Name` As User, pms_projects.* FROM pms_projects "
                + "INNER JOIN pms_users ON pms_projects.`ID_User` = pms_users.`ID_User` "
                +"WHERE pms_projects.`State` = " + estado + " AND CONCAT(pms_projects.`Name`, ' ', "
                + "pms_users.Name, ' ', pms_projects.Description,' ', pms_projects.Start_Date, ' ', "
                + "pms_projects.End_Date) LIKE '%" + busqueda + "%'";
        
        try{
            Statement st = getCon().createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            int contador = 0;
            while(rs.next()){ contador++; }
            registros = new String[6][contador];
            
            rs.first();            
            contador = 0;
            do{
                registros[0][contador] = rs.getString("ID_Project");                                 
                registros[1][contador] = rs.getString("User");
                registros[2][contador] = rs.getString("Name");
                registros[3][contador] = rs.getString("Description");
                registros[4][contador] = rs.getString("Start_Date");
                registros[5][contador] = rs.getString("End_Date");
                contador++;
            }while(rs.next());                        
        }catch(Exception e){                        
        }
        CerrarConexion();
        return registros;
    }        
    
    public String[][]misProyectos(int id, boolean admin){
        String[][] registros = null;
        String sSQL;
        if(admin)sSQL = "SELECT pms_users.`Name` As User, pms_projects.* FROM pms_projects "
                + "INNER JOIN pms_users ON pms_projects.`ID_User` = pms_users.`ID_User` "
                + "WHERE pms_projects.`State` = 1 AND pms_users.`ID_User` = " + id;
        else sSQL = "SELECT pms_users.`Name` As User, pms_custom.* "
                + "FROM(SELECT DISTINCT pms_projects.* FROM pms_userstasks "
                + "INNER JOIN pms_users ON pms_userstasks.ID_User = pms_users.ID_User "
                + "INNER JOIN pms_projects ON pms_userstasks.ID_Project = pms_projects.ID_Project "
                + "WHERE pms_users.ID_User = " + id + " AND pms_projects.State = 1) AS pms_custom "
                + "INNER JOIN pms_users ON pms_custom.ID_User = pms_users.`ID_User`";
        try{
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ResultSet rs = ps.executeQuery();

            int contador = 0;
            while(rs.next()){ contador++; }
            registros = new String[6][contador];

            rs.first();            
            contador = 0;
            do{
                registros[0][contador] = rs.getString("ID_Project");
                registros[1][contador] = rs.getString("User");
                registros[2][contador] = rs.getString("Name");
                registros[3][contador] = rs.getString("Description");
                registros[4][contador] = rs.getString("Start_Date");
                registros[5][contador] = rs.getString("End_Date");
                contador++;
            }while(rs.next());
        }catch(Exception e){}
        CerrarConexion();
        return registros;
    }
    
    public boolean edit(int id, String[] datos){
        String sSQL = "UPDATE pms_projects SET ID_User = ?, Name = ?, Description = ?, "
                    + "Start_Date = ?, End_Date = ? WHERE ID_Project = " + id;

        try {            
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ps.setInt(1, Integer.parseInt(datos[0]));
            ps.setString(2, datos[1]);
            ps.setString(3, datos[2]);
            ps.setString(4, datos[3]);
            ps.setString(5, datos[4]);                                            
            ps.execute();
            CerrarConexion();
            return true;           
        } catch (Exception ex) {                
            CerrarConexion();
            return false;
        }                    
    }
    
    public boolean restore(int id){
        String sSQL = "UPDATE pms_projects SET State = 1 WHERE ID_Project = " + id;
        try{
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ps.execute();
            CerrarConexion();
            return true;
        }catch(Exception e){
            CerrarConexion();
            return false;
        }
    }
    
    public boolean trash(int id){
        String sSQL = "UPDATE pms_projects SET State = 0 WHERE ID_Project = " + id;
        try{
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ps.execute();
                        
            //Buscar ID de la Tarea
            sSQL = "SELECT ID_Task FROM pms_tasks WHERE ID_Project = " + id;
            ps = getCon().prepareStatement(sSQL);
            ResultSet rs = ps.executeQuery(sSQL);
            
            while(rs.next()){
                int id_t = rs.getInt("ID_Task");
                new Tareas().trash(id_t);
            }
            
            CerrarConexion();
            return true;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "error en proyectoss", JOptionPane.ERROR_MESSAGE);
            CerrarConexion();
            return false;
        }
    }
    
    public boolean delete(int id){        
        String sSQL = "DELETE FROM pms_projects WHERE ID_Project = " + id;
        try{            
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ps.execute();
            
            //Buscar ID de la Tarea
            sSQL = "SELECT ID_Task FROM pms_tasks WHERE ID_Project = " + id;
            ps = getCon().prepareStatement(sSQL);
            ResultSet rs = ps.executeQuery(sSQL);
            
            while(rs.next()){
                int id_t = rs.getInt("ID_Task");
                new Tareas().delete(id_t);
            }
            
            CerrarConexion();            
            return true;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "error en proyectoss", JOptionPane.ERROR_MESSAGE);
            CerrarConexion();
            return false;
        }
    }
}
