/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.db.resolver.query;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mule.module.db.domain.query.BulkQuery;
import org.mule.module.db.parser.QueryTemplateParser;
import org.mule.module.db.util.FileReader;
import org.mule.tck.size.SmallTest;

import java.io.IOException;

import org.junit.Test;

@SmallTest
public class FileBulkQueryResolverTestCase extends AbstractBulkQueryResolverTestCase
{

    @Test
    public void doesNotResolvesBulkQueryWhenThereIsNoEvent() throws Exception
    {
        BulkQueryResolver bulkQueryResolver = new FileBulkQueryResolver(null, null, null);

        BulkQuery resolvedBulkQuery = bulkQueryResolver.resolve(null);

        assertThat(resolvedBulkQuery, nullValue());
    }

    @Test
    public void resolvesBulkQuery() throws Exception
    {
        String fileName = "fileName";

        QueryTemplateParser queryTemplateParser = createQueryTemplateParser();
        FileReader fileReader = mock(FileReader.class);
        when(fileReader.getResourceAsString(fileName)).thenReturn(BULK_SQL_QUERY);

        BulkQueryResolver bulkQueryResolver = new FileBulkQueryResolver(fileName, queryTemplateParser, fileReader);

        BulkQuery resolvedBulkQuery = bulkQueryResolver.resolve(muleEvent);

        assertResolvedBulkQuery(resolvedBulkQuery);
    }

    @Test(expected = QueryResolutionException.class)
    public void throwsErrorOnEmptyBulkQuery() throws Exception
    {
        String fileName = "fileName";

        FileReader fileReader = mock(FileReader.class);
        when(fileReader.getResourceAsString(fileName)).thenReturn("");

        FileBulkQueryResolver bulkQueryResolver = new FileBulkQueryResolver(fileName, null, fileReader);

        bulkQueryResolver.resolve(muleEvent);
    }

    @Test(expected = QueryResolutionException.class)
    public void throwsErrorOnFileReadError() throws Exception
    {
        String fileName = "fileName";

        FileReader fileReader = mock(FileReader.class);
        when(fileReader.getResourceAsString(fileName)).thenThrow(new IOException("Error"));

        FileBulkQueryResolver bulkQueryResolver = new FileBulkQueryResolver(fileName, null, fileReader);

        bulkQueryResolver.resolve(muleEvent);
    }
}
