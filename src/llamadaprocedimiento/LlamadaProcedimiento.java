package llamadaprocedimiento;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class LlamadaProcedimiento {

    public static void main(String[] args) {
        // TODO code application logic here
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));       
        
        int id = -1;
        int id1 = 10;
        
        Connection conexion = null;

        try {
            // Carga el driver de oracle
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            
            /* Conecta con la base de datos XE con el usuario system y la contraseña password
            Dos formas de conexion dependiendo el tipo de conexion
            1.- Si usamos nombre de servicio: jdbc:oracle:thin:@//Nombre del host:número de puerto/nombre de servicio
            2.- Si usamo SID: jdbc:oracle:thin:@//Nombre del host:número de puerto/nombre de servicio
            */
            conexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE", "system", "root");
            
            // Llamando al procedimiento almacenado
            CallableStatement procedimiento = conexion.prepareCall("{call ObtenerDatosAlumno (?,?,?,?)}");

            do {
                System.out.println("Por favor ingrese el Id del alumno a consultar en la BD:");
                try {
                    id = Integer.parseInt(entrada.readLine());
                } catch (IOException ex) {
                    System.out.println("Error al intentar...");
                }
                
                // Parametro 1 del procedimiento almacenado
                procedimiento.setInt(1, id);
                
                // Definimos los tipos de los parametros de salida del procedimiento almacenado
                procedimiento.registerOutParameter(2, java.sql.Types.VARCHAR);
                procedimiento.registerOutParameter(3, java.sql.Types.VARCHAR);
                procedimiento.registerOutParameter(4, java.sql.Types.VARCHAR);
                
                // Ejecuta el procedimiento almacenado
                procedimiento.execute();
                
                // Se obtienen la salida del procedimineto almacenado
                String nombre = procedimiento.getString(2);
                String sexo = procedimiento.getString(3);
                String curso = procedimiento.getString(4);
                System.out.println("Nombre: " + nombre);
                System.out.println("Sexo: " + sexo);
                System.out.println("Curso: " + curso);
            } while (id > 0);

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            try {
                conexion.close();
            } catch (SQLException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}