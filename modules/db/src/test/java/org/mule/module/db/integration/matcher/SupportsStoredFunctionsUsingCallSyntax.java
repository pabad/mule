/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.db.integration.matcher;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.sql.DataSource;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

public class SupportsStoredFunctionsUsingCallSyntax extends TypeSafeMatcher<DataSource>
{

    @Override
    public boolean matchesSafely(DataSource dataSource)
    {
        boolean supportFeature = false;

        Connection connection = null;
        try
        {
            try
            {
                connection = dataSource.getConnection();
                DatabaseMetaData metaData;
                metaData = connection.getMetaData();
                try
                {
                    supportFeature = metaData.supportsStoredFunctionsUsingCallSyntax();
                }
                catch (Throwable t)
                {
                    // Ignore
                }
            }
            finally
            {
                if (connection != null)
                {
                    connection.close();
                }
            }
        }
        catch (Exception e)
        {
            // Ignore
        }

        return supportFeature;
    }

    @Override
    public void describeTo(Description description)
    {
        description.appendText("database.supportsStoredFunctionsUsingCallSyntax == true");
    }
}
