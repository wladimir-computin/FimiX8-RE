package com.fimi.kernel.network.okhttp.cookie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpCookie;

public class SerializableHttpCookie implements Serializable {
    private static final long serialVersionUID = 6374381323722046732L;
    private transient HttpCookie clientCookie;
    private final transient HttpCookie cookie;

    public SerializableHttpCookie(HttpCookie cookie) {
        this.cookie = cookie;
    }

    public HttpCookie getCookie() {
        HttpCookie bestCookie = this.cookie;
        if (this.clientCookie != null) {
            return this.clientCookie;
        }
        return bestCookie;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(this.cookie.getName());
        out.writeObject(this.cookie.getValue());
        out.writeObject(this.cookie.getComment());
        out.writeObject(this.cookie.getCommentURL());
        out.writeObject(this.cookie.getDomain());
        out.writeLong(this.cookie.getMaxAge());
        out.writeObject(this.cookie.getPath());
        out.writeObject(this.cookie.getPortlist());
        out.writeInt(this.cookie.getVersion());
        out.writeBoolean(this.cookie.getSecure());
        out.writeBoolean(this.cookie.getDiscard());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.clientCookie = new HttpCookie((String) in.readObject(), (String) in.readObject());
        this.clientCookie.setComment((String) in.readObject());
        this.clientCookie.setCommentURL((String) in.readObject());
        this.clientCookie.setDomain((String) in.readObject());
        this.clientCookie.setMaxAge(in.readLong());
        this.clientCookie.setPath((String) in.readObject());
        this.clientCookie.setPortlist((String) in.readObject());
        this.clientCookie.setVersion(in.readInt());
        this.clientCookie.setSecure(in.readBoolean());
        this.clientCookie.setDiscard(in.readBoolean());
    }
}
