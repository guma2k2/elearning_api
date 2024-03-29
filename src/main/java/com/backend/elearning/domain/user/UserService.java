package com.backend.elearning.domain.user;

import com.backend.elearning.domain.common.PageableData;

public interface UserService {
    PageableData<UserVm> getUsers(int pageNum, int pageSize);
    UserGetDetailVm getUser(Long userId);
    UserVm create (UserPostVm userPostVm);
    void update (UserPutVm userPutVm, Long userId);
    void delete (Long userId);
}
