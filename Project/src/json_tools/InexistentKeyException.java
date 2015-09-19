package json_tools;

public class InexistentKeyException extends RuntimeException {
public InexistentKeyException(){
	super();
}
public InexistentKeyException(String message){
	super(message);
}
}
