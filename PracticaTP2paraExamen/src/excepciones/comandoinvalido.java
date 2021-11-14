package excepciones;

public class comandoinvalido extends Exception{
	
	private static final long serialVersionUID = 1L;

	public comandoinvalido() {super("El comando es invalido");}

	public comandoinvalido(String mensaje) {super(mensaje);}
	
	public comandoinvalido(String mensaje , Throwable razon) {super(mensaje, razon);}
	
	public comandoinvalido(Throwable razon) {super(razon);}
}
