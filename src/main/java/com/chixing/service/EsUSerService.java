package com.chixing.service;

import com.chixing.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Repository
public interface EsUSerService extends ElasticsearchRepository<User,Integer> {
    List<User> findByName(String userName);
    List<User> findByInfo(String userInfo);
}
