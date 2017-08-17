package com.jf.oauth.auth.dao.impl;

import org.springframework.stereotype.Repository;

import com.jf.oauth.auth.dao.ApiTokenDao;
import com.jf.oauth.core.entity.ApiToken;
import com.jf.oauth.core.tools.StringTools;

/**
 * API的Token信息
 * @author qiph
 * @version 1.0
 */
@Repository
public class ApiTokenDaoImpl extends BaseDaoImpl<ApiToken> implements ApiTokenDao {
	protected String NAMESPACE = "com.jf.oauth.auth.dao.ApiTokenDao.";

    @Override
    public String getNameSpace() {
        if (StringTools.isEmpty(NAMESPACE))
            return super.NAMESPACE;
        return NAMESPACE;
    }
}
