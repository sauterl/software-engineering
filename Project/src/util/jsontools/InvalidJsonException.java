package util.jsontools;

public class InvalidJsonException extends RuntimeException {
public InvalidJsonException(String msg){
	super(msg);
}
public InvalidJsonException(){
	super();
}
}
