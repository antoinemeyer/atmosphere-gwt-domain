package com.am.atmospheregwtdomain.server;

/**
 * The {@link ClientId} is required to interact with a specific client via the {@link AtmosphereManager}<br><br>
 * It is created from the concatenation of the session id and the connection id to obtain a unique identifier. Use {@link #getClientId(String, Integer)}
 * @author Antoine Meyer
 */
public class ClientId implements Comparable<ClientId>{

	private String id;

	/**
	 * Constructor<br>
	 * use {@link #getClientId(String, Integer)}
	 * @param string
	 */
	private ClientId(String string) {
		id = string;
	}

	@Override
    public int compareTo(ClientId a) {
        if(id != null) {
            return id.compareTo(a.getId());
        }
        return 1;
    }

    @Override
    public boolean equals(Object anObject) {
        if(this == anObject) {
            return true;
        }
        if(anObject == null || getClass() != anObject.getClass()) {
            return false;
        }

        ClientId a = (ClientId) anObject;
        return id.equals(a.getId());
    }
    
	@Override
	public String toString() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Generates a client id from the session id and connection id
	 * @param sessionid
	 * @param connectionId
	 * @return
	 */
	public static ClientId getClientId(String sessionid, Integer connectionId) {
		return new ClientId(sessionid + "-" + connectionId);
	}
	
}
