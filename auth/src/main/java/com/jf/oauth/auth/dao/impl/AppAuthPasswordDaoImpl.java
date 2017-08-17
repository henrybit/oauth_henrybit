package com.jf.oauth.auth.dao.impl;

import org.springframework.stereotype.Repository;

import com.jf.oauth.auth.dao.AppAuthPasswordDao;
import com.jf.oauth.core.entity.AppAuthPassword;
import com.jf.oauth.core.tools.StringTools;

/**
 * OAuth1.0密码登陆模式
 * @author qiph
 * @version 1.0
 */
@Repository
public class AppAuthPasswordDaoImpl extends BaseDaoImpl<AppAuthPassword> implements AppAuthPasswordDao{
	protected String NAMESPACE = "com.jf.oauth.auth.dao.AppAuthPasswordDao.";

    @Override
    public String getNameSpace() {
        if (StringTools.isEmpty(NAMESPACE))
            return super.NAMESPACE;
        return NAMESPACE;
    }
}
