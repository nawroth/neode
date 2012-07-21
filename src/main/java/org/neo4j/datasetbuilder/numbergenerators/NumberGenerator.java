/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.datasetbuilder.numbergenerators;

import java.util.List;
import java.util.Random;

public interface NumberGenerator
{
    List<Integer> generate( int minNumberOfResults, int maxNumberOfResults, int min, int max, Random random );
    List<Integer> generate( int numberOfResults, int min, int max, Random random );
    int generateSingle( int min, int max, Random random );
}