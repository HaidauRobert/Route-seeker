package customExceptions;

import utils.AlertMessage;

public class InvalidChoseCircleException extends Exception {

    public InvalidChoseCircleException() {
        AlertMessage.messageBox("You have to press on a specific circle");
    }
}
