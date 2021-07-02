package data;

import java.io.Serializable;

public enum MessageFromServerToClient implements Serializable {

    INFO,
    SERVER_IS_SILENT,
    OK,
    ERROR,
    OBJECT_UPDATED,
    OBJECT_REMOVED,
    OBJECT_ADDED,
    MANY_OBJECTS_REMOVED,
    PRIVATE_COLLECTION_CLEARED,
    NOT_AUTHORISED,
    PRINT_LIST

}
