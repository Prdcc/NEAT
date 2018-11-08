package neat;

/**
 *
 * @author Enrico
 */
public class NEATException extends Exception {

    public static enum ExceptionCode{
        GENERICERROR("Something went wrong"), NODENOTFOUND("The required node doesn't exist"),
        EMPTYSPECIES("The species has no individuals");
        String message;
        private ExceptionCode(String msg){
            message = msg;
        }
    }
    public NEATException() {
    }

    public NEATException(ExceptionCode code) {
        super(code.message);
    }
}
