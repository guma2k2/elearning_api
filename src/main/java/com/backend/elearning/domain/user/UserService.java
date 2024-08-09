package com.backend.elearning.domain.user;

import com.backend.elearning.domain.common.PageableData;

public interface UserService {
    PageableData<UserVm> getUsers(int pageNum, int pageSize, String keyword);
    UserGetDetailVm getUser(Long userId);
    UserVm create (UserPostVm userPostVm);
    UserVm update (UserPutVm userPutVm, Long userId);
    void delete (Long userId);
    UserProfileVM getById(Long userId);
    User getByEmail(String email);
    UserGetDetailVm getUserProfile(Long id);
}
