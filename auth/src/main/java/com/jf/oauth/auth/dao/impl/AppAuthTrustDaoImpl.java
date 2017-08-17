package com.jf.oauth.auth.dao.impl;

import org.springframework.stereotype.Repository;

import com.jf.oauth.auth.dao.AppAuthTrustDao;
import com.jf.oauth.core.entity.AppAuthTrust;
import com.jf.oauth.core.tools.StringTools;

/**
 * 服务器互信Dao<br>
 * @author qiph
 * @version 1.0
 */
@Repository
public class AppAuthTrustDaoImpl extends BaseDaoImpl<AppAuthTrust> implements AppAuthTrustDao {
	protected String NAMESPACE = "com.jf.oauth.auth.dao.AppAuthTrustDao.";

    @Override
    public String getNameSpace() {
        if (StringTools.isEmpty(NAMESPACE))
            return super.NAMESPACE;
        return NAMESPACE;
    }
}
