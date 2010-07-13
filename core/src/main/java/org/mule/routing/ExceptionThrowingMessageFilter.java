/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.routing;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.routing.filter.Filter;
import org.mule.api.routing.filter.FilterUnacceptedException;
import org.mule.config.i18n.CoreMessages;

public class ExceptionThrowingMessageFilter extends MessageFilter
{

    public ExceptionThrowingMessageFilter(Filter filter)
    {
        super(filter);
    }

    protected MuleEvent handleUnaccepted(MuleEvent event) throws MuleException
    {
        throw new FilterUnacceptedException(CoreMessages.messageRejectedByFilter(), filter);
    }

}
