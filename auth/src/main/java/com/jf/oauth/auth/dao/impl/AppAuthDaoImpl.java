package com.jf.oauth.auth.dao.impl;

import com.jf.oauth.auth.dao.AppAuthDao;
import com.jf.oauth.core.entity.AppAuth;
import com.jf.oauth.core.tools.StringTools;

import org.springframework.stereotype.Repository;

/**
 * app权限Dao<br>
 * Created by henrybit on 2017/5/5.
 * @version 1.0
 */
@Repository
public class AppAuthDaoImpl extends BaseDaoImpl<AppAuth> implements AppAuthDao{
    protected String NAMESPACE = "com.jf.oauth.auth.dao.AppAuthDao.";

    @Override
    public String getNameSpace() {
        if (StringTools.isEmpty(NAMESPACE))
            return super.NAMESPACE;
        return NAMESPACE;
    }
}
