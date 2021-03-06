package com.googlecode.easyec.security.userdetails.cache;

import com.googlecode.easyec.caching.CacheService;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;

import static org.springframework.util.StringUtils.hasText;

/**
 * EC用户缓存对象类。
 * <p>
 * 此对象缓存基于缓存服务框架实现。
 * </p>
 *
 * @author JunJie
 */
public class EcUserCache implements UserCache {

    private static final String CACHE_NAME = "userCache";
    private String cacheName = CACHE_NAME;
    private CacheService cacheService;

    public EcUserCache(CacheService cacheService) {
        this(null, cacheService);
    }

    public EcUserCache(String cacheName, CacheService cacheService) {
        if (hasText(cacheName)) this.cacheName = cacheName;
        this.cacheService = cacheService;
    }

    public UserDetails getUserFromCache(String username) {
        if (cacheService != null && hasText(username)) {
            return (UserDetails) cacheService.get(cacheName, username);
        }

        return null;
    }

    public void putUserInCache(UserDetails user) {
        if (cacheService != null && user != null) {
            cacheService.put(cacheName, user.getUsername(), user);
        }
    }

    public void removeUserFromCache(String username) {
        if (cacheService != null && hasText(username)) {
            cacheService.removeValue(cacheName, username);
        }
    }
}
