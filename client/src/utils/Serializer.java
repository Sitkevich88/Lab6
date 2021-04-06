package utils;

import java.io.*;
import java.nio.ByteBuffer;

public class Serializer {

    public ByteBuffer serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        ByteBuffer byteBuffer = ByteBuffer.wrap(out.toByteArray());
        return  byteBuffer;
    }

    public Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
