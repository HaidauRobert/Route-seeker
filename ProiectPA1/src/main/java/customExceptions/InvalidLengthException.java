package customExceptions;

import utils.AlertMessage;

public class InvalidLengthException extends Exception{

    public InvalidLengthException(String length) {
        AlertMessage.messageBox("The length introduced has to be in [300, 10000] m.");

    }
}
