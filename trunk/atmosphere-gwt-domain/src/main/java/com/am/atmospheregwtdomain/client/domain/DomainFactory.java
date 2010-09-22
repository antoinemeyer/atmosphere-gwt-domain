package com.am.atmospheregwtdomain.client.domain;

/**
 * The DomainFactory creates new {@link Domain}s with a name. The
 * name should be unique to avoid 'collision' of events.
 * @see Domain
 */
public final class DomainFactory
{
    private DomainFactory() {}

    /**
     * Creates a new {@link Domain} with a name. The name should be unique
     * to avoid 'collision' of events.
     * @param aDomainName unique domain name
     * @return {@link Domain}
     */
    public static Domain getDomain(String aDomainName) {
        return new DefaultDomain(aDomainName);
    }
}