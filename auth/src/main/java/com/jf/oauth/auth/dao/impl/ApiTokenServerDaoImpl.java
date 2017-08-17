package com.jf.oauth.auth.dao.impl;

import org.springframework.stereotype.Repository;

import com.jf.oauth.auth.dao.ApiTokenServerDao;
import com.jf.oauth.core.entity.ApiTokenServer;
import com.jf.oauth.core.tools.StringTools;

/**
 * Token相关API接口信息
 * @author qiph
 * @version 1.0
 */
@Repository
public class ApiTokenServerDaoImpl extends BaseDaoImpl<ApiTokenServer> implements ApiTokenServerDao {
	protected String NAMESPACE = "com.jf.oauth.auth.dao.ApiTokenServerDao.";

    @Override
    public String getNameSpace() {
        if (StringTools.isEmpty(NAMESPACE))
            return super.NAMESPACE;
        return NAMESPACE;
    }
}
