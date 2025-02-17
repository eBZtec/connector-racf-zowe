package com.evolveum.polygon.connector.racf.rest.util;

import com.evolveum.polygon.connector.racf.rest.api.RGroup;
import com.evolveum.polygon.connector.racf.rest.api.RObject;
import com.evolveum.polygon.connector.racf.rest.api.RUser;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.InvalidAttributeValueException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.Uid;

import java.util.Set;

public abstract class AbstractRacfRestConnector {

    private static final Log LOG = Log.getLog(AbstractRacfRestConnector.class);

    private <T> void validator(T object) {
        if (object == null) {
            LOG.error("Parameter type " + object.getClass().getSimpleName() + " not provided.");
            throw new InvalidAttributeValueException("Parameter type " + object.getClass().getSimpleName() + " not provided.");
        }
    }

    protected <T> void validate(T... objects) {

        for(T object: objects) {
            validator(object);
        }
    }

    protected abstract void handleGenericException(Exception ex, String message);

    protected abstract RUser translateUser(Uid uid, Set<Attribute> attributes);

    protected abstract RGroup translateGroup(Uid uid, Set<Attribute> attributes);

    protected abstract ConnectorObject translate(RObject object);
}
