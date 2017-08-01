package com.jf.oauth.dao.impl;

import com.jf.oauth.dao.AppAuthCodeDao;
import com.jf.oauth.entity.AppAuthCode;
import com.jf.oauth.tools.StringTools;
import org.springframework.stereotype.Repository;

/**
 * app授权code与token相关Dao<br>
 * Created by henrybit on 2017/5/5.
 * @version 1.0
 */
@Repository
public class AppAuthCodeDaoImpl extends BaseDaoImpl<AppAuthCode> implements AppAuthCodeDao {
    protected String NAMESPACE = "com.jf.oauth.dao.AppAuthCodeDao.";

    @Override
    public String getNameSpace() {
        if (StringTools.isEmpty(NAMESPACE))
            return super.NAMESPACE;
        return NAMESPACE;
    }
}
