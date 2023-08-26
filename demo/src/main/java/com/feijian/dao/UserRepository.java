package com.feijian.dao;

import com.feijian.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {
    public List<User> getUserByRoles(String role);
    public User getUserByUsername(String username);
    public List<User> getUsersByEnabled(boolean enabled);
}
