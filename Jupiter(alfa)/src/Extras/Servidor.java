package Extras;
import BaseDatos.*;
import ExApp.*;
import javax.swing.JOptionPane;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

/**
 * Clase con el código de los servicios que brinda el servidor (dar de alta, modificar, eliminar, etc).
 * @author Isay
 */
class Servicio extends _ExImplBase{    
    
    public boolean Crear (String[] datos, byte tabla){
        if(tabla == 1) return new Proyectos().save(datos);
        else if(tabla == 2) return new Tareas().save(datos);
        else return new Usuarios().save(datos);
    }
    
    public boolean Eliminar (int id, boolean permanente, byte tabla){
        if(tabla == 1){
            if(!permanente) return new Proyectos().trash(id);
            else return new Proyectos().delete(id);
        }else if(tabla == 2){
            if(!permanente) return new Tareas().trash(id);
            else return new Tareas().delete(id);
        }else{
            if(!permanente) return new Usuarios().trash(id);
            else return new Usuarios().delete(id);
        }
    }
    
    public String[] Login(String user, String pwd){               
        return new Usuarios().login(user, pwd);
    }
    
    public boolean Modificar (int id, String[] datos, byte tabla){
        if(tabla == 1) return new Proyectos().edit(id, datos);
        else if(tabla == 2) return new Tareas().edit(id, datos);
        else return new Usuarios().edit(id, datos);
    }
    
     public boolean ModificarUsuarioPwd (int id, String oldPwd, String newPwd){
        return new Usuarios().password(id, oldPwd, newPwd);
    }                  
    
    public String[][] Obtener (String busqueda, byte estado, byte tabla){
        if(tabla == 1) return new Proyectos().get(busqueda, estado);
        else if(tabla == 2) return new Tareas().get(busqueda, estado);
        else return new Usuarios().get(busqueda, estado);
    }
    
    public String[][] ObtenerUsuarioPorTarea(int id){
        return new Usuarios().getUserByIDTask(id);
    }              
                          
    public String[][]ObtenerMisProyectos(int id, boolean admin){
        return new Proyectos().misProyectos(id, admin);
    }        
    
    public String[][] ObtenerMisTareas(int id, boolean admin){
        return new Tareas().misTareas(id, admin);
    }
    
    public String[] ObtenerUsuarioPorID(int id, byte estado){
        return new Usuarios().getUserByID(id, estado);
    }
    
    public boolean Restaurar(int id, byte tabla){
        if(tabla == 1) return new Proyectos().restore(id);
        else if(tabla == 2) return new Tareas().restore(id);
        else return new Usuarios().restore(id);
    }        
}

/**
 * Clase con el código de CORBA y el ORB
 * @author Isay
 */
public class Servidor {    
    public static void main(String args[]) {
        try{
            String args2[] = {"-ORBInitialPort","1050","-ORBInitialHost","148.213.226.37"};
            ORB orb = ORB.init(args2, null);
                        
            Servicio exRef = new Servicio();
            orb.connect(exRef);
                        
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContext ncRef = NamingContextHelper.narrow(objRef);
                        
            NameComponent nc = new NameComponent("Ex", "");
            NameComponent path[] = {nc};
            ncRef.rebind(path, exRef);
                        
            java.lang.Object sync = new java.lang.Object();
            synchronized(sync){
              sync.wait();
            }
        }catch(Exception e){
            System.err.println("ERROR: 2" + e);            
        }
    }
}
