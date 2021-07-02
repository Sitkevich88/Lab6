package data;

import java.io.Serializable;

public enum AuthorisationResult implements Serializable {

    OK,
    SIGN_IN_ERROR,
    SIGN_UP_ERROR;

    public static final long serialVersionUID = 129877L;
}
