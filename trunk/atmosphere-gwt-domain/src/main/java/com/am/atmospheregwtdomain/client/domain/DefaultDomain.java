package com.am.atmospheregwtdomain.client.domain;

/**
 * Default implementation for a {@link Domain}
 * @author Dev1
 */
public final class DefaultDomain implements Domain
{
    private String myName;

    /**
     * @deprecated That constructor is only for serialization! Please use {@link #DefaultDomain(String)} instead.
     * @see #DefaultDomain(String)
     */
    @Deprecated
    public DefaultDomain() {}

    /**
     * Creates a new domain with a specified name. The name should be unique to avoid 'collision' of events.
     * @param aName unique domain name
     */
    public DefaultDomain(String aName) {
        myName = aName;
    }

    /**
     * Return the name of the domain.
     * @return domain name
     */
    public String getName() {
        return myName;
    }

    public int compareTo(Domain aDomain) {
        if(aDomain != null) {
            return myName.compareTo(aDomain.getName());
        }
        return 1;
    }

    public boolean equals(Object anObject) {
        if(this == anObject) {
            return true;
        }
        if(anObject == null || getClass() != anObject.getClass()) {
            return false;
        }

        Domain theDomain = (Domain)anObject;
        return myName.equals(theDomain.getName());
    }

    public int hashCode() {
        return myName.hashCode();
    }

    public String toString() {
        return myName;
    }
}