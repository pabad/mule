/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.db.integration.update;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mule.module.db.integration.DbTestUtil.selectData;
import static org.mule.module.db.integration.TestRecordUtil.assertRecords;
import org.mule.api.MuleMessage;
import org.mule.api.client.LocalMuleClient;
import org.mule.module.db.domain.autogeneratedkey.AutoGeneratedKeyStrategy;
import org.mule.module.db.domain.connection.DbConnection;
import org.mule.module.db.domain.query.QueryTemplate;
import org.mule.module.db.domain.statement.QueryStatementFactory;
import org.mule.module.db.integration.AbstractDbIntegrationTestCase;
import org.mule.module.db.integration.model.AbstractTestDatabase;
import org.mule.module.db.integration.model.Field;
import org.mule.module.db.integration.model.Record;
import org.mule.module.db.integration.TestDbConfig;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runners.Parameterized;

public class UpdateCustomStatementFactoryTestCase extends AbstractDbIntegrationTestCase
{

    private static boolean factoryInvoked;

    public UpdateCustomStatementFactoryTestCase(String dataSourceConfigResource, AbstractTestDatabase testDatabase)
    {
        super(dataSourceConfigResource, testDatabase);
    }

    @Parameterized.Parameters
    public static List<Object[]> parameters()
    {
        return TestDbConfig.getResources();
    }

    @Override
    protected String[] getFlowConfigurationResources()
    {
        return new String[] {"integration/update/update-custom-statement-factory-config.xml"};
    }

    @Test
    public void updatesDataRequestResponse() throws Exception
    {
        LocalMuleClient client = muleContext.getClient();
        MuleMessage response = client.send("vm://updateRequestResponse", TEST_MESSAGE, null);

        assertEquals(1, response.getPayload());
        List<Map<String, String>> result = selectData("select * from PLANET where POSITION=4", getDefaultDataSource());
        assertRecords(result, new Record(new Field("NAME", "Mercury"), new Field("POSITION", 4)));

        assertThat(factoryInvoked, equalTo(true));
    }

    public static class TestStatementFactory extends QueryStatementFactory
    {

        @Override
        public Statement create(DbConnection connection, QueryTemplate queryTemplate) throws SQLException
        {
            factoryInvoked = true;
            return super.create(connection, queryTemplate);
        }

        @Override
        public Statement create(DbConnection connection, QueryTemplate queryTemplate, AutoGeneratedKeyStrategy autoGeneratedKeyStrategy) throws SQLException
        {
            factoryInvoked = true;
            return super.create(connection, queryTemplate, autoGeneratedKeyStrategy);
        }
    }
}
