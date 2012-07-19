package org.neo4j.datasetbuilder;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

public class BatchCommandExecutor
{
    private final GraphDatabaseService db;
    private final Log log;

    public BatchCommandExecutor( GraphDatabaseService db, Log log )
    {
        this.db = db;
        this.log = log;
    }

    public <T> Results<T> execute( BatchCommand<T> command )
    {
        long startTime = System.nanoTime();

        log.write( "Begin [" + command.description() + "]" );
        for ( int index = 0; index < command.numberOfIterations(); index += command.batchSize() )
        {
            doExecute( index, command, startTime );
        }
        log.write( "End   [" + command.description() + "] (" + elapsedTime( startTime ) +")" );
        log.write( "" );
        return command;
    }

    private void doExecute( int startIndex, BatchCommand command, long startTime )
    {
        log.write( "       " + startIndex + "/" + command.numberOfIterations() +
                " [batch size: " + command.batchSize() + "] (" + elapsedTime( startTime ) + ")" );
        Transaction tx = db.beginTx();
        try
        {
            for ( int index = startIndex; indexIsInRange( startIndex, command, index ); index++ )
            {
                command.execute( db, index );
                tx.success();
            }
        }
        finally
        {
            tx.finish();
        }
    }

    private static boolean indexIsInRange( int startIndex, BatchCommand command, int index )
    {
        return index < (startIndex + command.batchSize()) && index < command.numberOfIterations();
    }

    private static String elapsedTime( long startTime )
    {
        return String.valueOf( (System.nanoTime() - startTime) / 1000000 );
    }

}