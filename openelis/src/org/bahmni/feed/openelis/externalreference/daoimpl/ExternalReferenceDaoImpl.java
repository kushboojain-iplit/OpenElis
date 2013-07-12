package org.bahmni.feed.openelis.externalreference.daoimpl;


import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.util.List;

public class ExternalReferenceDaoImpl extends BaseDAOImpl implements ExternalReferenceDao {

    @Override
    public boolean insertData(ExternalReference externalReference) throws LIMSRuntimeException {

        try {
            String id = (String) HibernateUtil.getSession().save(externalReference);
            externalReference.setId(id);

            HibernateUtil.getSession().flush();
            HibernateUtil.getSession().clear();
        } catch (Exception e) {
            LogEvent.logError("ExternalReferenceDaoImpl", "insertData()", e.toString());
            throw new LIMSRuntimeException("Error in ExternalReference insertData()", e);
        }

        return true;
    }

    @Override
    public ExternalReference getData(String externalReferenceId) throws LIMSRuntimeException {
            String sql = "from ExternalReference e where e.externalId =:param ";
            org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
            query.setParameter("param", externalReferenceId);

            List refs = query.list();
            HibernateUtil.getSession().flush();
            HibernateUtil.getSession().clear();
            if(!refs.isEmpty())
                 return (ExternalReference)refs.get(0);
            return null;

    }

    public void deleteData(ExternalReference data) throws LIMSRuntimeException {
            data = getData(data.getExternalId());
            if(data != null) {
                HibernateUtil.getSession().delete(data);
                HibernateUtil.getSession().flush();
                HibernateUtil.getSession().clear();
            }
    }

 }