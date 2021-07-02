package gui.authorisation;

public class CredentialsValidator {

    public boolean validate(String login, String password){

        boolean goodCondition = validateLogin(login) && validatePassword(password);

        if (goodCondition) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateLogin(String login){

        boolean badCondition = login == null || login.length()<5 || login.length()>50;

        if (badCondition){
            return false;
        }else {
            return true;
        }
    }

    public boolean validatePassword(String password){
        return validateLogin(password);
    }

}
