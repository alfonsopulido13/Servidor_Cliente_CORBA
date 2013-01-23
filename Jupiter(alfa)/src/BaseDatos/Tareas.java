package BaseDatos;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Isay
 */
public class Tareas extends BDMySQL{
    public Tareas(){ super(); }
    public Tareas(String db, String ip_loc, String user, String pwd){ super(db, ip_loc, user, pwd); }
    
    private String find(int id_p, String nombre, byte estado){
        String dato;
        String sSQL = "SELECT * FROM pms_tasks WHERE ID_Project = " + id_p + " AND Title = '" + nombre + "'"
                + " AND State = " + estado;
        
        try{            
            Statement st = getCon().createStatement();                        
            ResultSet rs = st.executeQuery(sSQL);
            
            rs.first();
            dato = rs.getString("ID_Task");
        }catch(Exception e){
            dato = "";
        }        
        CerrarConexion();        
        return dato;
    }
    
    public boolean save(String[] datos){
        if(!find(Integer.parseInt(datos[0]), datos[1], (byte)1).equals("")){
            CerrarConexion();
            return false;
        }else AbrirConexion();
        
        String sSQL = "INSERT INTO pms_tasks(ID_Project, Title, Description, Start_Date, End_Date, Priority, "
                + "State, Done) VALUES(?, ?, ?, ?, ?, ?, 1, 'Creada')";
                        
        try {            
            PreparedStatement ps = getCon().prepareStatement(sSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, Integer.parseInt(datos[0]));
            ps.setString(2, datos[1]);
            ps.setString(3, datos[2]);
            ps.setString(4, datos[3]);
            ps.setString(5, datos[4]);
            ps.setString(6, datos[5]);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            rs.first();
            int idTask = rs.getInt(1);            
            
            String[] idsUsers = datos[7].split(",");
            for(int i = 0; i < idsUsers.length; i++){
                sSQL = "INSERT INTO pms_userstasks(ID_User, ID_Task, ID_Project) VALUES(?, ?, ?)";            

                ps = getCon().prepareStatement(sSQL);
                ps.setInt(1, Integer.parseInt(idsUsers[i]));
                ps.setInt(2, idTask);
                ps.setInt(3, Integer.parseInt(datos[0]));
                ps.execute();
            }
                
            CerrarConexion();
            return true;            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            CerrarConexion();
            return false;
        }
    }    
    
    public String[][] get(String busqueda, byte estado){
        String[][] registros = null;               
        String sSQL = "SELECT pms_projects.`Name` As Project, pms_tasks.* FROM pms_tasks "
                + "INNER JOIN pms_projects ON pms_tasks.`ID_Project` = pms_projects.`ID_Project` "
                + "WHERE pms_tasks.`State` = " + estado + " AND CONCAT(pms_tasks.`Title`, ' ', pms_projects.Name, ' ', "
                + "pms_tasks.Description,' ', pms_tasks.Start_Date, ' ', pms_tasks.End_Date, ' ', "
                + "pms_tasks.Priority, ' ', pms_tasks.Done) LIKE '%" + busqueda + "%'";

        try{
            Statement st = getCon().createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            int contador = 0;
            while(rs.next()){ contador++; }
            registros = new String[8][contador];
            
            rs.first();            
            contador = 0;            
            do{
                registros[0][contador] = rs.getString("ID_Task");
                registros[1][contador] = rs.getString("Project");
                registros[2][contador] = rs.getString("Title");
                registros[3][contador] = rs.getString("Description");
                registros[4][contador] = rs.getString("Start_Date");
                registros[5][contador] = rs.getString("End_Date");                
                registros[6][contador] = rs.getString("Priority");
                registros[7][contador] = rs.getString("Done");
                contador++;
            }while(rs.next());                        
        }catch(Exception e){            
        }
        
        CerrarConexion();
        return registros;
    }
    
    public String[][] misTareas(int id, boolean admin){
        String[][] registros = null;
        String sSQL;
        if(admin) sSQL = "SELECT pms_projects.`Name` As Project, pms_tasks.* FROM pms_tasks "
                + "INNER JOIN pms_projects ON pms_tasks.`ID_Project` = pms_projects.`ID_Project` "
                + "WHERE pms_tasks.`State` = 1 AND pms_projects.`ID_Project` = " + id;
        else sSQL = "SELECT pms_projects.`Name` As Project, pms_tasks.* FROM pms_userstasks "
                + "INNER JOIN pms_users ON pms_userstasks.ID_User = pms_users.ID_User "
                + "INNER JOIN pms_tasks ON pms_userstasks.ID_Task = pms_tasks.ID_Task "
                + "INNER JOIN pms_projects ON pms_tasks.`ID_Project` = pms_projects.`ID_Project`"                
                + "WHERE pms_users.ID_User = " + id + " AND pms_tasks.State = 1;";
        try{
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ResultSet rs = ps.executeQuery();

            int contador = 0;
            while(rs.next()){ contador++; }
            registros = new String[8][contador];

            rs.first();            
            contador = 0;
            do{                    
                registros[0][contador] = rs.getString("ID_Task");                    
                registros[1][contador] = rs.getString("Project");
                registros[2][contador] = rs.getString("Title");
                registros[3][contador] = rs.getString("Description");
                registros[4][contador] = rs.getString("Start_Date");
                registros[5][contador] = rs.getString("End_Date");                
                registros[6][contador] = rs.getString("Priority");
                registros[7][contador] = rs.getString("Done");
                contador++;
            }while(rs.next());                
        }catch(Exception e){}
        
        CerrarConexion();
        return registros;
    }
    
    public boolean edit(int id, String[] datos){        
        String sSQL = "UPDATE pms_tasks SET ID_Project = ?, Title = ?, Description = ?, Start_Date = ?, "
                + "End_Date = ?, Priority = ?, Done = ? WHERE ID_Task = " + id;

        try {            
            PreparedStatement ps = getCon().prepareStatement(sSQL);
            ps.setInt(1, Integer.parseInt(datos[0]));
            ps.setString(2, datos[1]);
            ps.setString(3, datos[2]);
            ps.setString(4, datos[3]);
            ps.setString(5, datos[4]);
            ps.setString(6, datos[5]);
            ps.setString(7, datos[6]);
            ps.execute();
            
            sSQL = "DELETE FROM pms_userstasks WHERE ID_Task = " + id;
            ps = getCon().prepareStatement(sSQL);
            ps.execute();
            
            String[] idsUsers = datos[7].split(",");
            for(int i = 0; i < idsUsers.length; i++){
                sSQL = "INSERT INTO pms_userstasks(ID_User, ID_Task, ID_Project) VALUES(?, ?, ?)";            

                ps = getCon().prepareStatement(sSQL);
                ps.setInt(1, Integer.parseInt(idsUsers[i]));
                ps.setInt(2, id);
                ps.setInt(3, Integer.parseInt(datos[0]));
                ps.execute();
            }                        
            
            CerrarConexion();
            return true;
        } catch (Exception ex) {
            CerrarConexion();
            return false;
        }    
    }
    
    public boolean restore(int id){        
        String sSQL = "UPDATE pms_tasks SET State = 1 WHERE ID_Task = " + id;
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
        String sSQL = "UPDATE pms_tasks SET State = 0 WHERE ID_Task = " + id;
        try{
            PreparedStatement ps = getCon().prepareStatement(sSQL);            
            ps.execute();                        
            
            CerrarConexion();
            return true;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "error en tareas", JOptionPane.ERROR_MESSAGE);
            CerrarConexion();
            return false;
        }
    }
    
    public boolean delete(int id){
        String sSQL = "DELETE FROM pms_tasks WHERE ID_Task = " + id;
        try{
            PreparedStatement ps = getCon().prepareStatement(sSQL);            
            ps.execute();
            
            sSQL = "DELETE FROM pms_userstasks WHERE ID_Task = " + id;
            ps = getCon().prepareStatement(sSQL);            
            ps.execute();
            
            CerrarConexion();
            return true;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "error en tareas", JOptionPane.ERROR_MESSAGE);
            CerrarConexion();
            return false;
        }
    }
}
