package net.catharos.lib.database;

import gnu.trove.map.hash.THashMap;
import net.catharos.lib.core.lang.Closable;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.types.UInteger;

import java.sql.Timestamp;

/**
 * Provides queries
 */
public abstract class QueryProvider implements Closable {

    private final DSLProvider provider;
    private THashMap<QueryKey, QueryBuilder> builders = new THashMap<QueryKey, QueryBuilder>();

    public static final byte[] DEFAULT_BYTE_ARRAY = new byte[0];
    public static final byte[] DEFAULT_UUID = DEFAULT_BYTE_ARRAY;
    public static final String DEFAULT_STRING = "";
    public static final UInteger DEFAULT_UINTEGER = UInteger.valueOf(0);
    public static final Double DEFAULT_DOUBLE = 0d;
    public static final Timestamp DEFAULT_TIMESTAMP = new Timestamp(System.currentTimeMillis());

    protected QueryProvider(DSLProvider provider) {
        this.provider = provider;
        build();
    }

    public abstract void build();

    /**
     * Caches one specific query builder.
     *
     * @param key    The key of the query
     * @param builder The query builder
     */
    public final <Q extends Query> void builder(QueryKey<Q> key, QueryBuilder<Q> builder) {
        builders.put(key, builder);
    }

    /**
     * Gets/Creates a query.
     *
     * @param key  The key of the query
     * @param <Q> The type of the query
     * @return The query from the cache
     */
    public <Q extends Query> Q getQuery(QueryKey<Q> key) {
        QueryBuilder query = builders.get(key);

        if (query == null) {
            throw new IllegalStateException("Query " + key + " not found!");
        }

        //todo cache results, but only for specific threads and thread unique
        return key.toQuery(query.create(provider.getDSLContext()));
    }

    @Override
    public boolean close() {
        builders.clear();
        return true;
    }

    protected static interface QueryBuilder<Q extends Query> {

        Q create(DSLContext context);

    }
}

