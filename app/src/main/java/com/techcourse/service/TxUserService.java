package com.techcourse.service;

import com.techcourse.config.DataSourceConfig;
import com.techcourse.dao.UserDao;
import com.techcourse.dao.UserHistoryDao;
import com.techcourse.domain.User;
import com.techcourse.domain.UserHistory;

public class TxUserService implements UserService {

    private final AppUserService appUserService;
    private final CustomTransactionManager customTransactionManager = new CustomTransactionManager(DataSourceConfig.getInstance());

    public TxUserService(final AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    public User findById(final long id) {
        return appUserService.findById(id);
    }

    @Override
    public void save(final User user) {
        appUserService.save(user);
    }

    public void changePassword(final long id, final String newPassword, final String createBy) {
        customTransactionManager.executeTransaction(() -> {
            appUserService.changePassword(id, newPassword, createBy);
        });
    }
}
