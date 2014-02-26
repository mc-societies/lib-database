package net.catharos.lib.database;

import org.jooq.DSLContext;

/**
 * Represents a DSLProvider
 */
public interface DSLProvider {

    DSLContext getDSLContext();
}
