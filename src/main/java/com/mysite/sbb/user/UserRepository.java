package com.mysite.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;

                                        //SiteUser 의 PK 타입은 Long 이므로.
public interface UserRepository extends JpaRepository<SiteUser, Long> {

}
