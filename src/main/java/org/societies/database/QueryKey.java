package org.societies.database;

import net.catharos.lib.core.util.CastSafe;
import org.jooq.Query;

/**
 * Represents a QueryKey
 */
public class QueryKey<Q extends Query> {

    public static <Q extends Query> QueryKey<Q> create() {
        return new QueryKey<Q>();
    }

    public Q toQuery(Query query) {
        try {
            return CastSafe.toGeneric(query);
        } catch (ClassCastException e) {
            throw new IllegalStateException(
                    "You provided the wrong query type! The actual type is: " + query.getClass().getSimpleName(), e
            );
        }
    }
}
